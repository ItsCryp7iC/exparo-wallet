package com.exparo.drivebackup.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.exparo.drivebackup.worker.ScheduledBackupWorkerManual
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduledBackupRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val WORK_NAME = "scheduled_backup"
    }
    
    fun scheduleBackup(frequency: BackupFrequency, time: String, encrypt: Boolean) {
        val workManager = WorkManager.getInstance(context)
        
        // CRITICAL: Cancel any existing work first to ensure clean slate
        android.util.Log.d("ScheduledBackup", "Cancelling any existing backup work...")
        cancelScheduledBackup()
        
        // Small delay to ensure cancellation is processed
        Thread.sleep(100)
        
        // Parse time string (format: "HH:MM")
        val timeParts = time.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 2
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
        
        // Create constraints - More lenient for reliable execution
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Still need network for backup
            .setRequiresBatteryNotLow(false) // Allow backup even on low battery
            .setRequiresCharging(false) // Allow backup without charging
            .setRequiresDeviceIdle(false) // Allow backup when device is active
            .setRequiresStorageNotLow(true) // Ensure sufficient storage for backup
            .build()
        
        // Create input data
        val inputData = Data.Builder()
            .putBoolean(ScheduledBackupWorkerManual.INPUT_ENCRYPT, encrypt)
            .build()
        
        // Calculate initial delay to reach the specified time
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(java.util.Calendar.MINUTE, minute)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        
        // If the time has already passed today, schedule for tomorrow
        // Use a smaller buffer (30 seconds) to allow closer scheduling
        if (calendar.timeInMillis <= (now + 30000)) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
            android.util.Log.d("ScheduledBackup", "Target time has passed or is too close, scheduling for tomorrow")
        }
        
        val initialDelay = calendar.timeInMillis - now
        
        // Add debugging logs
        android.util.Log.d("ScheduledBackup", "Scheduling backup: frequency=$frequency, time=$time, hour=$hour, minute=$minute")
        android.util.Log.d("ScheduledBackup", "Current time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(now))}")
        android.util.Log.d("ScheduledBackup", "Target time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.time)}")
        android.util.Log.d("ScheduledBackup", "Initial delay: ${initialDelay}ms (${initialDelay / 1000 / 60} minutes)")
        
        // Only apply minimum delay if the user-specified delay is unreasonably small (< 2 minutes)
        val finalInitialDelay = if (initialDelay < 120000) { // Less than 2 minutes
            android.util.Log.d("ScheduledBackup", "Initial delay very small (${initialDelay}ms), setting to 2 minutes for safety")
            120000L // 2 minutes minimum
        } else {
            android.util.Log.d("ScheduledBackup", "Using user-specified delay: ${initialDelay}ms (${initialDelay / 1000 / 60} minutes)")
            initialDelay
        }
        
        // Create work request based on frequency
        val workRequest = when (frequency) {
            BackupFrequency.DAILY -> {
                PeriodicWorkRequestBuilder<ScheduledBackupWorkerManual>(1, TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .setInitialDelay(finalInitialDelay, TimeUnit.MILLISECONDS)
                    .build()
            }
            BackupFrequency.WEEKLY -> {
                PeriodicWorkRequestBuilder<ScheduledBackupWorkerManual>(7, TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .setInitialDelay(finalInitialDelay, TimeUnit.MILLISECONDS)
                    .build()
            }
            BackupFrequency.MONTHLY -> {
                PeriodicWorkRequestBuilder<ScheduledBackupWorkerManual>(30, TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .setInitialDelay(finalInitialDelay, TimeUnit.MILLISECONDS)
                    .build()
            }
        }
        
        // Enqueue the work with REPLACE policy to ensure clean scheduling
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE, // Changed from UPDATE to REPLACE
            workRequest
        )
        
        // Add debugging log
        android.util.Log.d("ScheduledBackup", "Work scheduled successfully: $WORK_NAME (REPLACE policy)")
        
        // Wait a moment for work to be registered
        Thread.sleep(200)
        
        // Check if work is actually scheduled
        val workInfos = workManager.getWorkInfosForUniqueWork(WORK_NAME).get()
        android.util.Log.d("ScheduledBackup", "Current work infos after scheduling: ${workInfos.size} jobs")
        workInfos.forEach { workInfo ->
            android.util.Log.d("ScheduledBackup", "Work: ${workInfo.id}, State: ${workInfo.state}, Tags: ${workInfo.tags}")
            // Add more detailed logging for all states
            when (workInfo.state) {
                androidx.work.WorkInfo.State.ENQUEUED -> {
                    android.util.Log.d("ScheduledBackup", "✅ Work successfully enqueued and ready to run")
                }
                androidx.work.WorkInfo.State.FAILED -> {
                    android.util.Log.e("ScheduledBackup", "❌ Work failed immediately - this indicates a serious problem")
                    android.util.Log.e("ScheduledBackup", "Failed work output: ${workInfo.outputData}")
                    android.util.Log.e("ScheduledBackup", "Failed work tags: ${workInfo.tags}")
                }
                else -> {
                    android.util.Log.d("ScheduledBackup", "Work state: ${workInfo.state}")
                }
            }
        }
    }
    
    fun cancelScheduledBackup() {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(WORK_NAME)
    }
    
    fun isBackupScheduled(): Boolean {
        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosForUniqueWork(WORK_NAME).get()
        val isScheduled = workInfos.isNotEmpty() && workInfos.first().state.isFinished.not()
        
        // Add debugging log
        android.util.Log.d("ScheduledBackup", "isBackupScheduled: $isScheduled, workInfos count: ${workInfos.size}")
        workInfos.forEach { workInfo ->
            android.util.Log.d("ScheduledBackup", "WorkInfo: ${workInfo.id}, State: ${workInfo.state}, Tags: ${workInfo.tags}")
        }
        
        return isScheduled
    }
    
    fun getScheduledBackupStatus(): String {
        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosForUniqueWork(WORK_NAME).get()
        
        return if (workInfos.isEmpty()) {
            "No scheduled backup found"
        } else {
            val workInfo = workInfos.first()
            "Backup scheduled: ${workInfo.state}, ID: ${workInfo.id}"
        }
    }
    
    fun getDetailedBackupStatus(): String {
        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosForUniqueWork(WORK_NAME).get()
        
        if (workInfos.isEmpty()) {
            return "❌ No scheduled backup found\n" +
                    "📝 Suggestion: Enable scheduled backups in settings"
        }
        
        val workInfo = workInfos.first()
        val now = System.currentTimeMillis()
        val nextRunTime = workInfo.nextScheduleTimeMillis
        
        return buildString {
            appendLine("📅 Backup Status: ${workInfo.state}")
            appendLine("🆔 Work ID: ${workInfo.id}")
            appendLine("⏰ Current Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(now))}")
            
            // Check if nextRunTime is available (WorkManager sometimes doesn't provide this)
            if (nextRunTime > 0) {
                appendLine("⏰ Next Run: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(nextRunTime))}")
                val delayMinutes = (nextRunTime - now) / 1000 / 60
                appendLine("⏳ Time until next run: ${delayMinutes} minutes")
            } else {
                appendLine("❓ Next Run Time: Not available (WorkManager limitation)")
            }
            
            appendLine("🏷️ Tags: ${workInfo.tags.joinToString(", ")}")
            appendLine("📊 Run Attempt Count: ${workInfo.runAttemptCount}")
            appendLine("📋 Progress: ${workInfo.progress}")
            
            if (workInfo.outputData.keyValueMap.isNotEmpty()) {
                appendLine("📤 Output Data: ${workInfo.outputData.keyValueMap}")
            }
            
            when (workInfo.state) {
                androidx.work.WorkInfo.State.ENQUEUED -> {
                    appendLine("✅ Status: Work is queued and waiting to run")
                }
                androidx.work.WorkInfo.State.RUNNING -> {
                    appendLine("🏃 Status: Work is currently running")
                }
                androidx.work.WorkInfo.State.SUCCEEDED -> {
                    appendLine("🎉 Status: Work completed successfully")
                }
                androidx.work.WorkInfo.State.FAILED -> {
                    appendLine("❌ Status: Work failed")
                    appendLine("💡 Troubleshooting:")
                    appendLine("   - Check if signed into Google Drive")
                    appendLine("   - Check network connectivity")
                    appendLine("   - Check battery optimization settings")
                    appendLine("   - Verify storage space available")
                }
                androidx.work.WorkInfo.State.BLOCKED -> {
                    appendLine("🚫 Status: Work is blocked (constraints not met)")
                    appendLine("💡 Possible causes:")
                    appendLine("   - No network connection")
                    appendLine("   - Low storage space")
                    appendLine("   - System in Doze mode")
                }
                androidx.work.WorkInfo.State.CANCELLED -> {
                    appendLine("🛑 Status: Work was cancelled")
                }
            }
        }
    }
    
    /**
     * Schedule a test backup that runs in 2 minutes for debugging purposes
     */
    fun scheduleTestBackup(encrypt: Boolean = true) {
        val workManager = WorkManager.getInstance(context)
        
        // Cancel existing backup first
        cancelScheduledBackup()
        
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val inputData = Data.Builder()
            .putBoolean(ScheduledBackupWorkerManual.INPUT_ENCRYPT, encrypt)
            .build()
        
        // Schedule to run in 2 minutes
        val testWorkRequest = PeriodicWorkRequestBuilder<ScheduledBackupWorkerManual>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .setInitialDelay(2, TimeUnit.MINUTES) // 2 minutes from now
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            "test_" + WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            testWorkRequest
        )
        
        android.util.Log.d("ScheduledBackup", "Test backup scheduled to run in 2 minutes")
    }
    
    /**
     * Run a one-time backup work immediately for testing
     */
    fun runImmediateTestWork() {
        val workManager = WorkManager.getInstance(context)
        
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val inputData = Data.Builder()
            .putBoolean(ScheduledBackupWorkerManual.INPUT_ENCRYPT, true)
            .build()
        
        // Create one-time work request
        val immediateWorkRequest = androidx.work.OneTimeWorkRequestBuilder<ScheduledBackupWorkerManual>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        
        workManager.enqueueUniqueWork(
            "immediate_test_backup",
            androidx.work.ExistingWorkPolicy.REPLACE,
            immediateWorkRequest
        )
        
        android.util.Log.d("ScheduledBackup", "Immediate test work enqueued")
        
        // Check status immediately
        val workInfos = workManager.getWorkInfosForUniqueWork("immediate_test_backup").get()
        workInfos.forEach { workInfo ->
            android.util.Log.d("ScheduledBackup", "Immediate work: ${workInfo.id}, State: ${workInfo.state}")
        }
    }
    
    /**
     * Test the exact timing calculation logic without scheduling
     */
    fun debugTimingCalculation(targetTime: String): String {
        val timeParts = targetTime.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 2
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
        
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(java.util.Calendar.MINUTE, minute)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        
        val originalTarget = calendar.timeInMillis
        
        // Apply same logic as scheduleBackup
        if (calendar.timeInMillis <= (now + 30000)) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }
        
        val initialDelay = calendar.timeInMillis - now
        
        val finalInitialDelay = if (initialDelay < 120000) {
            120000L
        } else {
            initialDelay
        }
        
        return buildString {
            appendLine("⏰ Timing Debug for: $targetTime")
            appendLine("🕐 Current Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(now))}")
            appendLine("🎯 Original Target: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(originalTarget))}")
            appendLine("📅 Final Target: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.time)}")
            appendLine("⏳ Initial Delay: ${initialDelay}ms (${initialDelay / 1000 / 60} minutes)")
            appendLine("✅ Final Delay: ${finalInitialDelay}ms (${finalInitialDelay / 1000 / 60} minutes)")
            appendLine("🔄 Same Day?: ${originalTarget == calendar.timeInMillis}")
        }
    }
}

enum class BackupFrequency {
    DAILY,
    WEEKLY,
    MONTHLY
}