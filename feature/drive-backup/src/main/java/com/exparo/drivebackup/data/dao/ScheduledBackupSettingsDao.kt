package com.exparo.drivebackup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exparo.drivebackup.data.model.ScheduledBackupSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledBackupSettingsDao {
    
    @Query("SELECT * FROM scheduled_backup_settings WHERE id = :id")
    fun getSettings(id: String = "default"): Flow<ScheduledBackupSettings?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: ScheduledBackupSettings)
    
    @Update
    suspend fun updateSettings(settings: ScheduledBackupSettings)
    
    @Query("DELETE FROM scheduled_backup_settings WHERE id = :id")
    suspend fun deleteSettings(id: String = "default")
}
