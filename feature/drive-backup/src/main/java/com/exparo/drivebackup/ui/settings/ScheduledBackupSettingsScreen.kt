package com.exparo.drivebackup.ui.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.exparo.design.l0_system.UI
import com.exparo.design.l0_system.style
import com.exparo.design.l1_buildingBlocks.IconScale
import com.exparo.design.l1_buildingBlocks.ExparoIconScaled
import com.exparo.design.utils.thenIf
import com.exparo.drivebackup.data.model.BackupFrequency
import com.exparo.drivebackup.data.model.ScheduledBackupSettings
import com.exparo.legacy.rootScreen
import com.exparo.navigation.navigation
import com.exparo.navigation.screenScopedViewModel
import com.exparo.ui.R
import com.exparo.wallet.ui.theme.Blue
import com.exparo.wallet.ui.theme.Gray
import com.exparo.wallet.ui.theme.White
import com.exparo.wallet.ui.theme.components.ExparoToolbar
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@ExperimentalFoundationApi
@Composable
fun BoxWithConstraintsScope.ScheduledBackupSettingsScreen() {
    val viewModel: ScheduledBackupSettingsViewModel = screenScopedViewModel()
    val uiState = viewModel.uiState()
    
    ScheduledBackupSettingsContent(
        settings = uiState.settings,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}

@Composable
fun ScheduledBackupSettingsContent(
    settings: ScheduledBackupSettings?,
    onEvent: (ScheduledBackupSettingsEvent) -> Unit
) {
    val nav = navigation()
    
    // State for dialogs
    var showFrequencyDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var showNumberDialog by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("scheduled_backup_settings_lazy_column")
    ) {
        item {
            ExparoToolbar(
                onBack = { nav.onBackPressed() },
            ) {
                Spacer(Modifier.weight(1f))
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = "Scheduled Backups",
                style = UI.typo.h2.style(
                    fontWeight = FontWeight.Black
                )
            )
            
            Spacer(Modifier.height(24.dp))
        }
        
        item {
            // Enable/disable scheduled backups
            SettingsSwitch(
                icon = R.drawable.ic_custom_calendar_m,
                text = "Enable Scheduled Backups",
                description = "Automatically create backups at scheduled times",
                isEnabled = settings?.isEnabled ?: false,
                onToggle = { enabled ->
                    onEvent(ScheduledBackupSettingsEvent.SetEnabled(enabled))
                }
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Frequency selection
            if (settings?.isEnabled == true) {
                SettingsDropdown(
                    icon = R.drawable.ic_custom_calendar_m,
                    text = "Backup Frequency",
                    description = "How often to create backups",
                    selectedValue = settings.frequency.name.lowercase().capitalize(),
                    onClick = { showFrequencyDialog = true }
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Time selection
                SettingsTimePicker(
                    icon = R.drawable.ic_custom_calendar_m,
                    text = "Backup Time",
                    description = "Time to create backups",
                    time = settings.time,
                    onClick = { showTimeDialog = true }
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Encryption toggle
                SettingsSwitch(
                    icon = R.drawable.ic_vue_security_shield,
                    text = "Encrypt Backups",
                    description = "Secure your backup files with encryption",
                    isEnabled = settings.encryptBackups,
                    onToggle = { enabled ->
                        onEvent(ScheduledBackupSettingsEvent.SetEncryptBackups(enabled))
                    }
                )
                
                Spacer(Modifier.height(12.dp))
                
                                 // Keep last N backups
                 SettingsNumberInput(
                     icon = R.drawable.ic_vue_security_shield,
                     text = "Keep Last Backups",
                     description = "Number of recent backups to keep",
                     value = settings.keepLastBackups,
                     onClick = { showNumberDialog = true }
                 )
                 
                                 Spacer(Modifier.height(12.dp))
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(120.dp)) // last item spacer
        }
    }
    
    // Frequency Selection Dialog
    if (showFrequencyDialog) {
        FrequencySelectionDialog(
            currentFrequency = settings?.frequency ?: BackupFrequency.WEEKLY,
            onFrequencySelected = { frequency ->
                onEvent(ScheduledBackupSettingsEvent.SetFrequency(frequency))
                showFrequencyDialog = false
            },
            onDismiss = { showFrequencyDialog = false }
        )
    }
    
    // Time Selection Dialog
    if (showTimeDialog) {
        TimeSelectionDialog(
            currentTime = settings?.time ?: "02:00",
            onTimeSelected = { time ->
                onEvent(ScheduledBackupSettingsEvent.SetTime(time))
                showTimeDialog = false
            },
            onDismiss = { showTimeDialog = false }
        )
    }
    
    // Keep Last Backups Dialog
    if (showNumberDialog) {
        KeepLastBackupsDialog(
            currentValue = settings?.keepLastBackups ?: 5,
            onValueSelected = { value ->
                onEvent(ScheduledBackupSettingsEvent.SetKeepLastBackups(value))
                showNumberDialog = false
            },
            onDismiss = { showNumberDialog = false }
        )
    }
}

@Composable
private fun SettingsSwitch(
    icon: Int,
    text: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    SettingsButtonRow(
        onClick = null // No click action for switch, use the switch itself
    ) {
        Spacer(Modifier.width(12.dp))
        
        ExparoIconScaled(
            icon = icon,
            tint = UI.colors.pureInverse,
            iconScale = IconScale.M,
            padding = 0.dp
        )
        
        Spacer(Modifier.width(8.dp))
        
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = text,
                style = UI.typo.b2.style(
                    color = UI.colors.pureInverse,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = description,
                style = UI.typo.nB2.style(
                    color = UI.colors.gray,
                    fontWeight = FontWeight.Normal
                ).copy(fontSize = 14.sp)
            )
        }
        
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Composable
private fun SettingsDropdown(
    icon: Int,
    text: String,
    description: String,
    selectedValue: String,
    onClick: () -> Unit
) {
    SettingsButtonRow(
        onClick = onClick
    ) {
        Spacer(Modifier.width(12.dp))
        
        ExparoIconScaled(
            icon = icon,
            tint = UI.colors.pureInverse,
            iconScale = IconScale.M,
            padding = 0.dp
        )
        
        Spacer(Modifier.width(8.dp))
        
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = text,
                style = UI.typo.b2.style(
                    color = UI.colors.pureInverse,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = description,
                style = UI.typo.nB2.style(
                    color = UI.colors.gray,
                    fontWeight = FontWeight.Normal
                ).copy(fontSize = 14.sp)
            )
        }
        
        Text(
            text = selectedValue,
            style = UI.typo.b2.style(
                color = UI.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
        
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
private fun SettingsTimePicker(
    icon: Int,
    text: String,
    description: String,
    time: String,
    onClick: () -> Unit
) {
    SettingsButtonRow(
        onClick = onClick
    ) {
        Spacer(Modifier.width(12.dp))
        
        ExparoIconScaled(
            icon = icon,
            tint = UI.colors.pureInverse,
            iconScale = IconScale.M,
            padding = 0.dp
        )
        
        Spacer(Modifier.width(8.dp))
        
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = text,
                style = UI.typo.b2.style(
                    color = UI.colors.pureInverse,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = description,
                style = UI.typo.nB2.style(
                    color = UI.colors.gray,
                    fontWeight = FontWeight.Normal
                ).copy(fontSize = 14.sp)
            )
        }
        
        Text(
            text = time,
            style = UI.typo.b2.style(
                color = UI.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
        
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
private fun SettingsNumberInput(
    icon: Int,
    text: String,
    description: String,
    value: Int,
    onClick: () -> Unit
) {
    SettingsButtonRow(
        onClick = onClick
    ) {
        Spacer(Modifier.width(12.dp))
        
        ExparoIconScaled(
            icon = icon,
            tint = UI.colors.pureInverse,
            iconScale = IconScale.M,
            padding = 0.dp
        )
        
        Spacer(Modifier.width(8.dp))
        
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = text,
                style = UI.typo.b2.style(
                    color = UI.colors.pureInverse,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = description,
                style = UI.typo.nB2.style(
                    color = UI.colors.gray,
                    fontWeight = FontWeight.Normal
                ).copy(fontSize = 14.sp)
            )
        }
        
        Text(
            text = if (value == -1) "All" else value.toString(),
            style = UI.typo.b2.style(
                color = UI.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
        
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
private fun SettingsButtonRow(
    onClick: (() -> Unit)?,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(UI.shapes.r4)
            .background(UI.colors.medium, UI.shapes.r4)
            .thenIf(onClick != null) {
                clickable {
                    onClick?.invoke()
                }
            },
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun FrequencySelectionDialog(
    currentFrequency: BackupFrequency,
    onFrequencySelected: (BackupFrequency) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Backup Frequency",
                style = UI.typo.h2.style(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                listOf("Daily", "Weekly", "Monthly").forEach { frequency ->
                    val backupFrequency = when (frequency.lowercase()) {
                        "daily" -> BackupFrequency.DAILY
                        "weekly" -> BackupFrequency.WEEKLY
                        "monthly" -> BackupFrequency.MONTHLY
                        else -> BackupFrequency.WEEKLY
                    }
                    
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFrequencySelected(backupFrequency) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = frequency,
                            style = UI.typo.b1.style(
                                color = if (currentFrequency == backupFrequency) UI.colors.primary else UI.colors.pureInverse,
                                fontWeight = if (currentFrequency == backupFrequency) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (currentFrequency == backupFrequency) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = UI.colors.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSelectionDialog(
    currentTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timeParts = currentTime.split(":")
    val currentHour = timeParts.getOrNull(0)?.toIntOrNull() ?: 2
    val currentMinute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
    
    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Backup Time",
                style = UI.typo.h2.style(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            TimePicker(
                state = timePickerState,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val timeString = String.format(
                        "%02d:%02d",
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    onTimeSelected(timeString)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun KeepLastBackupsDialog(
    currentValue: Int,
    onValueSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
        "10" to 10,
        "All" to -1
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Keep Last Backups",
                style = UI.typo.h2.style(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(options) { (label, value) ->
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onValueSelected(value) }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            style = UI.typo.b1.style(
                                color = if (value == currentValue) UI.colors.primary else UI.colors.pureInverse,
                                fontWeight = if (value == currentValue) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (value == currentValue) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = UI.colors.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
