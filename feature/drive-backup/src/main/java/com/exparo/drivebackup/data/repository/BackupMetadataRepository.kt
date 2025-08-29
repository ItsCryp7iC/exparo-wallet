package com.exparo.drivebackup.data.repository

import com.exparo.drivebackup.data.dao.BackupMetadataDao
import com.exparo.drivebackup.data.model.BackupMetadata
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupMetadataRepository @Inject constructor(
    private val backupMetadataDao: BackupMetadataDao
) {
    fun getAllBackups(): Flow<List<BackupMetadata>> {
        return backupMetadataDao.getAllBackups()
    }
    
    suspend fun getBackupById(id: String): BackupMetadata? {
        return backupMetadataDao.getBackupById(id)
    }
    
    suspend fun getBackupByDriveFileId(driveFileId: String): BackupMetadata? {
        return backupMetadataDao.getBackupByDriveFileId(driveFileId)
    }
    
    suspend fun insertBackup(backup: BackupMetadata) {
        backupMetadataDao.insertBackup(backup)
    }
    
    suspend fun updateBackup(backup: BackupMetadata) {
        backupMetadataDao.updateBackup(backup)
    }
    
    suspend fun deleteBackup(id: String) {
        backupMetadataDao.deleteBackup(id)
    }
    
    suspend fun deleteAllBackups() {
        backupMetadataDao.deleteAllBackups()
    }
}