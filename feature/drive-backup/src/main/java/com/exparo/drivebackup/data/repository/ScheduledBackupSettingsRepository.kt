package com.exparo.drivebackup.data.repository

import com.exparo.drivebackup.data.dao.ScheduledBackupSettingsDao
import com.exparo.drivebackup.data.model.ScheduledBackupSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduledBackupSettingsRepository @Inject constructor(
    private val scheduledBackupSettingsDao: ScheduledBackupSettingsDao
) {
    fun getSettings(id: String = "default"): Flow<ScheduledBackupSettings?> {
        return scheduledBackupSettingsDao.getSettings(id)
    }
    
    suspend fun insertSettings(settings: ScheduledBackupSettings) {
        scheduledBackupSettingsDao.insertSettings(settings)
    }
    
    suspend fun updateSettings(settings: ScheduledBackupSettings) {
        scheduledBackupSettingsDao.updateSettings(settings)
    }
    
    suspend fun deleteSettings(id: String = "default") {
        scheduledBackupSettingsDao.deleteSettings(id)
    }
}
