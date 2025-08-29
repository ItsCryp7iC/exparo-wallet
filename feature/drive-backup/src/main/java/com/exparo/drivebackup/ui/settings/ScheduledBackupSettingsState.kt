package com.exparo.drivebackup.ui.settings

import androidx.compose.runtime.Stable
import com.exparo.drivebackup.data.model.BackupFrequency
import com.exparo.drivebackup.data.model.ScheduledBackupSettings

@Stable
data class ScheduledBackupSettingsState(
    val settings: ScheduledBackupSettings?
)

sealed class ScheduledBackupSettingsEvent {
    data class SetEnabled(val enabled: Boolean) : ScheduledBackupSettingsEvent()
    data class SetFrequency(val frequency: BackupFrequency) : ScheduledBackupSettingsEvent()
    data class SetTime(val time: String) : ScheduledBackupSettingsEvent()
    data class SetEncryptBackups(val encrypt: Boolean) : ScheduledBackupSettingsEvent()
    data class SetKeepLastBackups(val count: Int) : ScheduledBackupSettingsEvent()

}
