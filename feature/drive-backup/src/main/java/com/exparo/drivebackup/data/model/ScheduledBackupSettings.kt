package com.exparo.drivebackup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_backup_settings")
data class ScheduledBackupSettings(
    @PrimaryKey
    val id: String = "default",
    val isEnabled: Boolean = false,
    val frequency: BackupFrequency = BackupFrequency.WEEKLY,
    val time: String = "02:00", // 24-hour format
    val encryptBackups: Boolean = true,
    val keepLastBackups: Int = 5
)

enum class BackupFrequency {
    DAILY,
    WEEKLY,
    MONTHLY
}
