package com.exparo.drivebackup.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.exparo.drivebackup.service.BackupManager
import com.exparo.drivebackup.service.GoogleDriveService
import com.exparo.drivebackup.data.repository.ScheduledBackupSettingsRepository
import kotlinx.coroutines.flow.firstOrNull

/**
 * Manual implementation of ScheduledBackupWorker without @HiltWorker
 * This bypasses Hilt dependency injection issues by using manual injection
 */
class ScheduledBackupWorkerManual(
    context: Context,
    workerParams: WorkerParameters,
    private val backupManager: BackupManager,
    private val googleDriveService: GoogleDriveService,
    private val scheduledBackupSettingsRepository: ScheduledBackupSettingsRepository
) : CoroutineWorker(context, workerParams) {
    
    init {
        try {
            android.util.Log.d("ScheduledBackupWorkerManual", "🔧 Manual worker constructor called - dependencies injected successfully")
            android.util.Log.d("ScheduledBackupWorkerManual", "BackupManager instance: ${backupManager}")
            android.util.Log.d("ScheduledBackupWorkerManual", "GoogleDriveService instance: ${googleDriveService}")
            android.util.Log.d("ScheduledBackupWorkerManual", "ScheduledBackupSettingsRepository instance: ${scheduledBackupSettingsRepository}")
        } catch (e: Exception) {
            android.util.Log.e("ScheduledBackupWorkerManual", "Error in worker constructor", e)
        }
    }
    
    companion object {
        const val INPUT_ENCRYPT = "encrypt"
    }
    
    override suspend fun doWork(): Result {
        return try {
            val startTime = System.currentTimeMillis()
            android.util.Log.d("ScheduledBackupWorkerManual", "🚀 === MANUAL WORKER EXECUTION STARTED ===")
            android.util.Log.d("ScheduledBackupWorkerManual", "Worker started at ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(startTime))}")
            android.util.Log.d("ScheduledBackupWorkerManual", "Worker run attempt: ${runAttemptCount}")
            android.util.Log.d("ScheduledBackupWorkerManual", "Worker input data: ${inputData.keyValueMap}")
            android.util.Log.d("ScheduledBackupWorkerManual", "Worker ID: ${id}")
            
            // Test dependency availability
            android.util.Log.d("ScheduledBackupWorkerManual", "Testing dependencies...")
            android.util.Log.d("ScheduledBackupWorkerManual", "BackupManager available: ${backupManager != null}")
            android.util.Log.d("ScheduledBackupWorkerManual", "GoogleDriveService available: ${googleDriveService != null}")
            android.util.Log.d("ScheduledBackupWorkerManual", "ScheduledBackupSettingsRepository available: ${scheduledBackupSettingsRepository != null}")
            
            // Check if user is signed in
            val isSignedIn = try {
                googleDriveService.isSignedIn()
            } catch (e: Exception) {
                android.util.Log.e("ScheduledBackupWorkerManual", "Error checking sign-in status", e)
                false
            }
            
            android.util.Log.d("ScheduledBackupWorkerManual", "User signed in: $isSignedIn")
            
            if (!isSignedIn) {
                android.util.Log.e("ScheduledBackupWorkerManual", "User not signed in to Google Drive")
                return Result.failure()
            }
            
            // Get encryption preference
            val encrypt = inputData.getBoolean(INPUT_ENCRYPT, true)
            android.util.Log.d("ScheduledBackupWorkerManual", "Encryption enabled: $encrypt")
            
            // Create backup
            android.util.Log.d("ScheduledBackupWorkerManual", "Starting backup creation...")
            val result = try {
                backupManager.createBackup(encrypt)
            } catch (e: Exception) {
                android.util.Log.e("ScheduledBackupWorkerManual", "Exception during backup creation", e)
                return Result.failure()
            }
            
            android.util.Log.d("ScheduledBackupWorkerManual", "Backup result: $result")
            
            when (result) {
                is BackupManager.BackupProgress.Completed -> {
                    android.util.Log.d("ScheduledBackupWorkerManual", "🎉 Backup completed successfully at ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
                    // Clean up old backups after successful backup
                    cleanupOldBackups()
                    Result.success()
                }
                is BackupManager.BackupProgress.Error -> {
                    android.util.Log.e("ScheduledBackupWorkerManual", "Backup failed with error: ${result.message}")
                    Result.failure()
                }
                else -> {
                    android.util.Log.e("ScheduledBackupWorkerManual", "Backup failed with unknown result: $result")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("ScheduledBackupWorkerManual", "Critical exception in doWork()", e)
            Result.failure()
        }
    }
    
    private suspend fun cleanupOldBackups() {
        try {
            // Get current settings
            val settings = scheduledBackupSettingsRepository.getSettings().firstOrNull()
            val keepLastBackups = settings?.keepLastBackups ?: 5
            
            // If keepLastBackups is -1 (All), don't clean up
            if (keepLastBackups == -1) {
                return
            }
            
            // Get all backups and sort by creation date (newest first)
            val allBackups = backupManager.getAllBackups()
            val sortedBackups = allBackups.sortedByDescending { it.createdDate }
            
            // Delete backups beyond the limit
            if (sortedBackups.size > keepLastBackups) {
                val backupsToDelete = sortedBackups.drop(keepLastBackups)
                for (backup in backupsToDelete) {
                    backupManager.deleteBackup(backup)
                }
            }
        } catch (e: Exception) {
            // Log error but don't fail the backup
            android.util.Log.e("ScheduledBackupWorkerManual", "Error cleaning up old backups", e)
        }
    }
}