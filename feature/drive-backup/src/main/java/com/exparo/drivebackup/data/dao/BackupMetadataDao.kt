package com.exparo.drivebackup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exparo.drivebackup.data.model.BackupMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface BackupMetadataDao {
    
    @Query("SELECT * FROM backup_metadata ORDER BY createdDate DESC")
    fun getAllBackups(): Flow<List<BackupMetadata>>
    
    @Query("SELECT * FROM backup_metadata WHERE id = :id")
    suspend fun getBackupById(id: String): BackupMetadata?
    
    @Query("SELECT * FROM backup_metadata WHERE driveFileId = :driveFileId")
    suspend fun getBackupByDriveFileId(driveFileId: String): BackupMetadata?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackup(backup: BackupMetadata)
    
    @Update
    suspend fun updateBackup(backup: BackupMetadata)
    
    @Query("DELETE FROM backup_metadata WHERE id = :id")
    suspend fun deleteBackup(id: String)
    
    @Query("DELETE FROM backup_metadata")
    suspend fun deleteAllBackups()
}