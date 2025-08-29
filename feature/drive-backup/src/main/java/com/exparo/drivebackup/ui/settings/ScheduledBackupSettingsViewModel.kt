package com.exparo.drivebackup.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.exparo.drivebackup.data.model.BackupFrequency
import com.exparo.drivebackup.data.model.ScheduledBackupSettings
import com.exparo.drivebackup.data.repository.ScheduledBackupSettingsRepository
import com.exparo.drivebackup.data.repository.ScheduledBackupRepository
import com.exparo.drivebackup.data.repository.BackupFrequency as RepositoryBackupFrequency
import com.exparo.ui.ComposeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class ScheduledBackupSettingsViewModel @Inject constructor(
    private val scheduledBackupSettingsRepository: ScheduledBackupSettingsRepository,
    private val scheduledBackupRepository: ScheduledBackupRepository
) : ComposeViewModel<ScheduledBackupSettingsState, ScheduledBackupSettingsEvent>() {
    
    private var settings by mutableStateOf<ScheduledBackupSettings?>(null)
    
    init {
        loadSettings()
    }
    
    @Composable
    override fun uiState(): ScheduledBackupSettingsState {
        return ScheduledBackupSettingsState(
            settings = this.settings
        )
    }
    
    override fun onEvent(event: ScheduledBackupSettingsEvent) {
        when (event) {
            is ScheduledBackupSettingsEvent.SetEnabled -> setEnabled(event.enabled)
            is ScheduledBackupSettingsEvent.SetFrequency -> setFrequency(event.frequency)
            is ScheduledBackupSettingsEvent.SetTime -> setTime(event.time)
            is ScheduledBackupSettingsEvent.SetEncryptBackups -> setEncryptBackups(event.encrypt)
            is ScheduledBackupSettingsEvent.SetKeepLastBackups -> setKeepLastBackups(event.count)

        }
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            scheduledBackupSettingsRepository.getSettings().collectLatest { newSettings ->
                settings = newSettings ?: ScheduledBackupSettings()
            }
        }
    }
    
    private fun setEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val currentSettings = settings ?: ScheduledBackupSettings()
            val updatedSettings = currentSettings.copy(isEnabled = enabled)
            scheduledBackupSettingsRepository.insertSettings(updatedSettings)
            settings = updatedSettings
            
            // Schedule or cancel backup based on enabled state
            if (enabled) {
                scheduleBackup(updatedSettings)
            } else {
                scheduledBackupRepository.cancelScheduledBackup()
            }
        }
    }
    
    private fun setFrequency(frequency: BackupFrequency) {
        viewModelScope.launch {
            val currentSettings = settings ?: ScheduledBackupSettings()
            val updatedSettings = currentSettings.copy(frequency = frequency)
            scheduledBackupSettingsRepository.insertSettings(updatedSettings)
            settings = updatedSettings
            
            // Reschedule backup if enabled
            if (updatedSettings.isEnabled) {
                scheduleBackup(updatedSettings)
            }
        }
    }
    
    private fun setTime(time: String) {
        viewModelScope.launch {
            val currentSettings = settings ?: ScheduledBackupSettings()
            val updatedSettings = currentSettings.copy(time = time)
            scheduledBackupSettingsRepository.insertSettings(updatedSettings)
            settings = updatedSettings
            
            // Reschedule backup if enabled
            if (updatedSettings.isEnabled) {
                scheduleBackup(updatedSettings)
            }
        }
    }
    
    private fun setEncryptBackups(encrypt: Boolean) {
        viewModelScope.launch {
            val currentSettings = settings ?: ScheduledBackupSettings()
            val updatedSettings = currentSettings.copy(encryptBackups = encrypt)
            scheduledBackupSettingsRepository.insertSettings(updatedSettings)
            settings = updatedSettings
            
            // Reschedule backup if enabled
            if (updatedSettings.isEnabled) {
                scheduleBackup(updatedSettings)
            }
        }
    }
    
    private fun setKeepLastBackups(count: Int) {
        viewModelScope.launch {
            val currentSettings = settings ?: ScheduledBackupSettings()
            val updatedSettings = currentSettings.copy(keepLastBackups = count)
            scheduledBackupSettingsRepository.insertSettings(updatedSettings)
            settings = updatedSettings
            
            // Note: keepLastBackups doesn't affect scheduling, just cleanup logic
        }
    }
    
    private fun scheduleBackup(settings: ScheduledBackupSettings) {
        // Convert our BackupFrequency to the repository's BackupFrequency
        val repositoryFrequency = when (settings.frequency) {
            BackupFrequency.DAILY -> RepositoryBackupFrequency.DAILY
            BackupFrequency.WEEKLY -> RepositoryBackupFrequency.WEEKLY
            BackupFrequency.MONTHLY -> RepositoryBackupFrequency.MONTHLY
        }
        
        scheduledBackupRepository.scheduleBackup(
            frequency = repositoryFrequency,
            time = settings.time,
            encrypt = settings.encryptBackups
        )
    }
    
    

}
