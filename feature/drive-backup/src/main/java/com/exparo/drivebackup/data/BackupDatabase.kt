package com.exparo.drivebackup.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exparo.drivebackup.data.dao.BackupMetadataDao
import com.exparo.drivebackup.data.dao.ScheduledBackupSettingsDao
import com.exparo.drivebackup.data.model.BackupMetadata
import com.exparo.drivebackup.data.model.ScheduledBackupSettings
import com.exparo.drivebackup.util.DateConverter

@Database(
    entities = [
        BackupMetadata::class,
        ScheduledBackupSettings::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class BackupDatabase : RoomDatabase() {
    abstract fun backupMetadataDao(): BackupMetadataDao
    abstract fun scheduledBackupSettingsDao(): ScheduledBackupSettingsDao
}