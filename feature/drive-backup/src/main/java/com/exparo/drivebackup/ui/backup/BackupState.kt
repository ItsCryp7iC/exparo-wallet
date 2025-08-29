package com.exparo.drivebackup.ui.backup

import com.exparo.drivebackup.data.model.BackupMetadata
import com.exparo.drivebackup.service.BackupManager

data class BackupState(
    val backups: List<BackupMetadata> = emptyList(),
    val isSignedIn: Boolean = false,
    val backupProgress: BackupManager.BackupProgress = BackupManager.BackupProgress.Idle,
    val selectedBackup: BackupMetadata? = null,
    val errorMessage: String? = null,
    val currentUserEmail: String? = null
)