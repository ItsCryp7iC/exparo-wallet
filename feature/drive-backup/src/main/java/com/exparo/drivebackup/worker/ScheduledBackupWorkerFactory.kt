package com.exparo.drivebackup.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.exparo.drivebackup.service.BackupManager
import com.exparo.drivebackup.service.GoogleDriveService
import com.exparo.drivebackup.data.repository.ScheduledBackupSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Custom WorkerFactory to manually inject dependencies for ScheduledBackupWorker
 * This bypasses the @HiltWorker annotation processing issues
 */
@Singleton
class ScheduledBackupWorkerFactory @Inject constructor(
    private val backupManager: BackupManager,
    private val googleDriveService: GoogleDriveService,
    private val scheduledBackupSettingsRepository: ScheduledBackupSettingsRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            ScheduledBackupWorker::class.java.name -> {
                // Manually create the worker with injected dependencies
                ScheduledBackupWorkerManual(
                    context = appContext,
                    workerParams = workerParameters,
                    backupManager = backupManager,
                    googleDriveService = googleDriveService,
                    scheduledBackupSettingsRepository = scheduledBackupSettingsRepository
                )
            }
            ScheduledBackupWorkerManual::class.java.name -> {
                // Manually create the worker with injected dependencies
                ScheduledBackupWorkerManual(
                    context = appContext,
                    workerParams = workerParameters,
                    backupManager = backupManager,
                    googleDriveService = googleDriveService,
                    scheduledBackupSettingsRepository = scheduledBackupSettingsRepository
                )
            }
            else -> null
        }
    }
}