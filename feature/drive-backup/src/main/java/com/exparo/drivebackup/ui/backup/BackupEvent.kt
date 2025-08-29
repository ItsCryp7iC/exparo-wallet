package com.exparo.drivebackup.ui.backup

import com.exparo.drivebackup.data.model.BackupMetadata
import com.google.android.gms.auth.api.signin.GoogleSignInAccount // <-- ADD THIS IMPORT

sealed class BackupEvent {
    object SignIn : BackupEvent()
    object SignOut : BackupEvent()
    data class CreateBackup(val encrypt: Boolean = true) : BackupEvent()
    data class RestoreBackup(val backup: BackupMetadata) : BackupEvent()
    data class DeleteBackup(val backup: BackupMetadata) : BackupEvent()
    data class SelectBackup(val backup: BackupMetadata?) : BackupEvent()
    object LoadBackups : BackupEvent()
    object RefreshBackups : BackupEvent()
    object CompleteOnboarding : BackupEvent()

    // ADD THIS NEW EVENT CLASS
    data class GoogleSignInResult(val success: Boolean, val account: GoogleSignInAccount?) : BackupEvent()
}