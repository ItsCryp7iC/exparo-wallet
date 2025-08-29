package com.exparo.drivebackup.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.exparo.drivebackup.service.BackupManager
import com.exparo.drivebackup.service.GoogleDriveService
import com.exparo.drivebackup.data.repository.ScheduledBackupSettingsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class ScheduledBackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val backupManager: BackupManager,
    private val googleDriveService: GoogleDriveService,
    private val scheduledBackupSettingsRepository: ScheduledBackupSettingsRepository
) : CoroutineWorker(context, workerParams) {
    
    init {
        try {
            android.util.Log.d("ScheduledBackupWorker", "Worker constructor called - dependencies injected successfully")
            android.util.Log.d("ScheduledBackupWorker", "BackupManager instance: ${backupManager}")
            android.util.Log.d("ScheduledBackupWorker", "GoogleDriveService instance: ${googleDriveService}")
            android.util.Log.d("ScheduledBackupWorker", "ScheduledBackupSettingsRepository instance: ${scheduledBackupSettingsRepository}")
        } catch (e: Exception) {
            android.util.Log.e("ScheduledBackupWorker", "Error in worker constructor", e)
        }
    }
    
    companion object {
        const val INPUT_ENCRYPT = "encrypt"
    }
    
    override suspend fun doWork(): Result {
        return try {
            val startTime = System.currentTimeMillis()
            android.util.Log.d("ScheduledBackupWorker", "=== WORKER EXECUTION STARTED ===")
            android.util.Log.d("ScheduledBackupWorker", "Worker started at ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(startTime))}")
            android.util.Log.d("ScheduledBackupWorker", "Worker run attempt: ${runAttemptCount}")
            android.util.Log.d("ScheduledBackupWorker", "Worker input data: ${inputData.keyValueMap}")
            android.util.Log.d("ScheduledBackupWorker", "Worker ID: ${id}")
            
            // Test dependency availability
            android.util.Log.d("ScheduledBackupWorker", "Testing dependencies...")
            android.util.Log.d("ScheduledBackupWorker", "BackupManager available: ${backupManager != null}")
            android.util.Log.d("ScheduledBackupWorker", "GoogleDriveService available: ${googleDriveService != null}")
            android.util.Log.d("ScheduledBackupWorker", "ScheduledBackupSettingsRepository available: ${scheduledBackupSettingsRepository != null}")
            
            // Check if user is signed in
            val isSignedIn = try {
                googleDriveService.isSignedIn()
            } catch (e: Exception) {
                android.util.Log.e("ScheduledBackupWorker", "Error checking sign-in status", e)
                false
            }
            
            android.util.Log.d("ScheduledBackupWorker", "User signed in: $isSignedIn")
            
            if (!isSignedIn) {
                android.util.Log.e("ScheduledBackupWorker", "User not signed in to Google Drive")
                return Result.failure()
            }
            
            // Get encryption preference
            val encrypt = inputData.getBoolean(INPUT_ENCRYPT, true)
            android.util.Log.d("ScheduledBackupWorker", "Encryption enabled: $encrypt")
            
            // Create backup
            android.util.Log.d("ScheduledBackupWorker", "Starting backup creation...")
            val result = try {
                backupManager.createBackup(encrypt)
            } catch (e: Exception) {
                android.util.Log.e("ScheduledBackupWorker", "Exception during backup creation", e)
                return Result.failure()
            }
            
            android.util.Log.d("ScheduledBackupWorker", "Backup result: $result")
            
            when (result) {
                is BackupManager.BackupProgress.Completed -> {
                    android.util.Log.d("ScheduledBackupWorker", "Backup completed successfully at ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
                    // Clean up old backups after successful backup
                    cleanupOldBackups()
                    Result.success()
                }
                is BackupManager.BackupProgress.Error -> {
                    android.util.Log.e("ScheduledBackupWorker", "Backup failed with error: ${result.message}")
                    Result.failure()
                }
                else -> {
                    android.util.Log.e("ScheduledBackupWorker", "Backup failed with unknown result: $result")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("ScheduledBackupWorker", "Critical exception in doWork()", e)
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
            android.util.Log.e("ScheduledBackupWorker", "Error cleaning up old backups", e)
        }
    }
}