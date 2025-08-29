package com.exparo.drivebackup.service

import android.content.Context
import android.net.Uri
import android.util.Log
import com.exparo.data.backup.BackupDataUseCase
import com.exparo.drivebackup.data.model.BackupMetadata
import com.exparo.drivebackup.data.repository.BackupMetadataRepository
import com.exparo.drivebackup.util.EncryptionUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.crypto.CipherOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleDriveService: GoogleDriveService,
    private val backupMetadataRepository: BackupMetadataRepository,
    private val backupDataUseCase: BackupDataUseCase,
    private val encryptionUtil: EncryptionUtil
) {
    private val _backupProgress = MutableStateFlow<BackupProgress>(BackupProgress.Idle)
    val backupProgress: StateFlow<BackupProgress> = _backupProgress.asStateFlow()

    companion object {
        private const val TAG = "BackupManager"
        private const val BACKUP_FOLDER_NAME = "ExparoWalletBackups"
        private const val MIME_TYPE_ZIP = "application/zip"
    }

    sealed class BackupProgress {
        object Idle : BackupProgress()
        data class InProgress(val progress: Double, val message: String) : BackupProgress()
        data class Completed(val backupMetadata: BackupMetadata) : BackupProgress()
        data class Error(val message: String) : BackupProgress()
    }

    suspend fun createBackup(encrypt: Boolean = true): BackupProgress {
        try {
            _backupProgress.value = BackupProgress.InProgress(0.0, "Starting backup...")

            val userId = googleDriveService.getCurrentUserId()
            if (userId == null) {
                val error = BackupProgress.Error("User not signed in.")
                _backupProgress.value = error
                return error
            }

            val backupDir = java.io.File(context.cacheDir, "backups")
            backupDir.mkdirs()

            val timestamp = System.currentTimeMillis()
            val finalFileName = "ExparoWalletBackup_$timestamp.zip"
            val finalFile = java.io.File(backupDir, finalFileName)

            var outputStream: OutputStream? = null
            try {
                outputStream = if (encrypt) {
                    _backupProgress.value = BackupProgress.InProgress(0.1, "Preparing encryption...")
                    val cipher = encryptionUtil.getEncryptionCipher(userId)
                    val fos = FileOutputStream(finalFile)
                    fos.write(cipher.iv)
                    CipherOutputStream(fos, cipher)
                } else {
                    FileOutputStream(finalFile)
                }
                _backupProgress.value = BackupProgress.InProgress(0.2, "Exporting data...")
                backupDataUseCase.exportToStream(outputStream)
            } finally {
                outputStream?.close()
            }

            _backupProgress.value = BackupProgress.InProgress(0.6, "Uploading to Google Drive...")
            val driveFileId = uploadToDrive(finalFile, finalFileName, encrypt)

            val backupMetadata = BackupMetadata(
                id = UUID.randomUUID().toString(),
                fileName = finalFileName,
                fileSize = finalFile.length(),
                createdDate = Date(timestamp),
                lastModifiedDate = Date(timestamp),
                driveFileId = driveFileId,
                isUploaded = driveFileId != null,
                isEncrypted = encrypt
            )

            backupMetadataRepository.insertBackup(backupMetadata)
            finalFile.delete()

            _backupProgress.value = BackupProgress.Completed(backupMetadata)
            return BackupProgress.Completed(backupMetadata)

        } catch (e: Exception) {
            Log.e(TAG, "Full stack trace for backup failure:", e)
            val error = BackupProgress.Error("Failed to create backup: ${e.message}")
            _backupProgress.value = error
            return error
        }
    }

    suspend fun restoreBackup(backupMetadata: BackupMetadata): BackupProgress {
        return try {
            _backupProgress.value = BackupProgress.InProgress(0.0, "Restoring backup...")

            val userId = googleDriveService.getCurrentUserId()
            if (userId == null) {
                return BackupProgress.Error("User not signed in.")
            }

            _backupProgress.value = BackupProgress.InProgress(0.2, "Downloading backup...")
            val driveService = googleDriveService.getDriveService() ?: return BackupProgress.Error("Not connected to Google Drive")
            val tempFile = java.io.File.createTempFile("restore_", ".zip", context.cacheDir)

            val fileId = backupMetadata.driveFileId
            if (fileId.isNullOrBlank()) {
                return BackupProgress.Error("Missing Drive fileId for the selected backup")
            }

            withContext(Dispatchers.IO) {
                driveService.files()
                    .get(fileId)
                    .executeMediaAndDownloadTo(FileOutputStream(tempFile))
            }

            val finalFile = if (backupMetadata.isEncrypted) {
                _backupProgress.value = BackupProgress.InProgress(0.5, "Decrypting data...")
                val decryptedFile = java.io.File.createTempFile("decrypted_", ".zip", context.cacheDir)
                encryptionUtil.decryptFile(tempFile, decryptedFile, userId)
                tempFile.delete()
                decryptedFile
            } else {
                tempFile
            }

            _backupProgress.value = BackupProgress.InProgress(0.8, "Importing data...")
            backupDataUseCase.importBackupFile(Uri.fromFile(finalFile)) { progress ->
                _backupProgress.value = BackupProgress.InProgress(0.8 + (progress * 0.2), "Importing data...")
            }

            finalFile.delete()

            _backupProgress.value = BackupProgress.Completed(backupMetadata)
            BackupProgress.Idle
        } catch (e: Exception) {
            Log.e(TAG, "Error restoring backup", e)
            val error = BackupProgress.Error("Failed to restore backup: ${e.message}")
            _backupProgress.value = error
            error
        }
    }

    private suspend fun uploadToDrive(file: java.io.File, fileName: String, isEncrypted: Boolean): String? {
        return try {
            val driveService = googleDriveService.getDriveService() ?: return null
            withContext(Dispatchers.IO) {
                val metadata = File().apply {
                    name = fileName
                    parents = listOf("appDataFolder")
                    description = if (isEncrypted) "Encrypted Exparo Wallet backup" else "Exparo Wallet backup"
                }
                val driveFile = driveService.files()
                    .create(metadata, com.google.api.client.http.FileContent(MIME_TYPE_ZIP, file))
                    .setFields("id, name, size, createdTime, modifiedTime")
                    .execute()
                driveFile.id
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading to Drive", e)
            null
        }
    }

    private suspend fun getOrCreateBackupFolder(driveService: Drive): String {
        try {
            val result = driveService.files().list()
                .setSpaces("appDataFolder") // Tell it to search the hidden folder
                .setQ("name = '$BACKUP_FOLDER_NAME' and mimeType = 'application/vnd.google-apps.folder' and trashed = false")
                .setFields("files(id, name)")
                .execute()
            if (result.files?.isNotEmpty() == true) {
                return result.files[0].id
            }
            val fileMetadata = File().apply {
                name = BACKUP_FOLDER_NAME
                mimeType = "application/vnd.google-apps.folder"
                parents = listOf("appDataFolder")
            }
            val folder = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute()
            return folder.id
        } catch (e: Exception) {
            Log.e(TAG, "Error creating/getting backup folder", e)
            throw e
        }
    }

    suspend fun deleteBackup(backupMetadata: BackupMetadata): Boolean {
        return try {
            val driveService = googleDriveService.getDriveService()
            if (driveService != null && backupMetadata.driveFileId != null) {
                withContext(Dispatchers.IO) {
                    driveService.files().delete(backupMetadata.driveFileId).execute()
                }
            }
            backupMetadataRepository.deleteBackup(backupMetadata.id)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting backup", e)
            false
        }
    }

    suspend fun getAllBackups(): List<BackupMetadata> {
        Log.d(TAG, "--- Starting getAllBackups sync ---")
        // Always start from what we have locally
        val localBefore = backupMetadataRepository.getAllBackups().first()
        Log.d(TAG, "Local backups before sync: ${localBefore.size}")
        
        val driveService = googleDriveService.getDriveService()
        if (driveService == null) {
            Log.d(TAG, "Drive service is null. Returning local backups only.")
            return localBefore
        }

        return try {
            // First, let's try to list ALL files in appDataFolder to see what's there
            Log.d(TAG, "Listing ALL files in appDataFolder...")
            val allFiles = withContext(Dispatchers.IO) {
                driveService.files().list()
                    .setSpaces("appDataFolder")
                    .setFields("files(id, name, size, createdTime, modifiedTime, description, mimeType)")
                    .execute()
                    .files
            }
            
            Log.d(TAG, "Total files in appDataFolder: ${allFiles?.size ?: 0}")
            allFiles?.forEach { file ->
                Log.d(TAG, "All file: ${file.name} (ID: ${file.id}, MIME: ${file.mimeType}, Size: ${file.size})")
            }

            // Now query for backup files specifically
            val queryString = "(mimeType = 'application/zip' or name contains 'ExparoWalletBackup') and trashed = false"
            Log.d(TAG, "Querying Drive appDataFolder for backup files with Q: $queryString")

            val driveFiles = withContext(Dispatchers.IO) {
                driveService.files().list()
                    .setSpaces("appDataFolder")
                    .setQ(queryString)
                    .setFields("files(id, name, size, createdTime, modifiedTime, description, mimeType)")
                    .execute()
                    .files
            }

            Log.d(TAG, "Google Drive API returned ${driveFiles?.size ?: 0} backup file(s).")
            driveFiles?.forEach { file ->
                Log.d(TAG, "Backup file: ${file.name} (ID: ${file.id}, Size: ${file.size}, Created: ${file.createdTime}, MIME: ${file.mimeType})")
            }

            val localBackupsMap = localBefore.associateBy { it.driveFileId }
            var newBackupsSynced = 0
            
            driveFiles?.forEach { driveFile ->
                if (!localBackupsMap.containsKey(driveFile.id)) {
                    newBackupsSynced++
                    Log.d(TAG, "Syncing new backup: ${driveFile.name}")
                    
                    // Determine if encrypted based on description or filename
                    val isEncrypted = driveFile.description?.contains("Encrypted") == true || 
                                    driveFile.name.contains("encrypted")
                    
                    val backupMetadata = BackupMetadata(
                        id = UUID.randomUUID().toString(),
                        fileName = driveFile.name,
                        fileSize = driveFile.size?.toLong() ?: 0L,
                        createdDate = Date(driveFile.createdTime.value),
                        lastModifiedDate = Date(driveFile.modifiedTime.value),
                        driveFileId = driveFile.id,
                        isUploaded = true,
                        isEncrypted = isEncrypted
                    )
                    backupMetadataRepository.insertBackup(backupMetadata)
                    Log.d(TAG, "Inserted backup: ${backupMetadata.fileName} with ID: ${backupMetadata.id}")
                }
            }
            Log.d(TAG, "Synced $newBackupsSynced new backup(s).")

            val finalList = backupMetadataRepository.getAllBackups().first()
            Log.d(TAG, "--- Finished getAllBackups sync. Returning ${finalList.size} total backups. ---")
            finalList
        } catch (e: Exception) {
            Log.e(TAG, "Error during getAllBackups sync:", e)
            Log.e(TAG, "Stack trace:", e)
            // If Drive fails, still return whatever we had locally
            backupMetadataRepository.getAllBackups().first()
        }
    }
}