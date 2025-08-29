# Google Drive Backup Feature

This module implements Google Drive backup and restore functionality for the Exparo Wallet app.

## Features

- **Google Sign-In Integration**: Secure authentication with Google accounts
- **Manual Backup**: Create encrypted backups of app data to Google Drive
- **Restore Functionality**: Restore app data from Google Drive backups
- **Scheduled Backups**: Configure automatic daily/weekly/monthly backups
- **Encryption**: Optional AES-256 encryption for backup files
- **Backup Management**: View, delete, and manage existing backups

## Architecture

### Components

1. **GoogleDriveService**: Handles Google Sign-In and Drive API operations
2. **BackupManager**: Manages backup creation, restoration, and scheduling
3. **BackupViewModel**: UI logic for the backup screen
4. **ScheduledBackupSettingsViewModel**: UI logic for scheduled backup settings
5. **EncryptionUtil**: Handles file encryption/decryption
6. **ScheduledBackupWorker**: Background worker for automatic backups

### Data Models

- `BackupMetadata`: Stores information about backup files
- `ScheduledBackupSettings`: Configuration for automatic backups
- `BackupFrequency`: Enum for backup frequency options

### Database

Uses Room database with the following tables:
- `backup_metadata`: Stores backup file information
- `scheduled_backup_settings`: Stores scheduled backup configuration

## Setup

### Google Cloud Console

1. Create a new project in Google Cloud Console
2. Enable Google Drive API
3. Create OAuth 2.0 credentials
4. Download `google-services.json` and place it in the `app/` directory

### Dependencies

The following dependencies are required:

```kotlin
// Google Drive API
implementation("com.google.android.gms:play-services-auth:21.2.0")
implementation("com.google.apis:google-api-services-drive:v3-rev20240508-2.0.0")
implementation("com.google.api-client:google-api-client-android:2.6.0")
implementation("com.google.http-client:google-http-client-gson:1.45.0")
```

## Usage

### Manual Backup

1. Navigate to Settings > Backup to Google Drive
2. Sign in with Google account
3. Tap "Create Backup" to create a new backup
4. Monitor progress and completion status

### Restore Backup

1. Navigate to Settings > Backup to Google Drive
2. Select a backup from the list
3. Tap the restore icon to restore data
4. Monitor progress and completion status

### Scheduled Backups

1. Navigate to Settings > Backup to Google Drive
2. Tap "Scheduled Backups"
3. Enable scheduled backups
4. Configure frequency, time, and encryption settings

## Security

- All backup files are optionally encrypted using AES-256-GCM
- Google Sign-In uses OAuth 2.0 for secure authentication
- Backup files are stored in a dedicated Google Drive folder
- Encryption keys are managed securely

## Error Handling

The feature includes comprehensive error handling for:
- Network connectivity issues
- Google Sign-In failures
- Drive API errors
- File encryption/decryption errors
- Database operation failures

## Testing

The module includes unit tests for:
- Backup creation and restoration
- Encryption utilities
- Database operations
- ViewModel logic

## Future Enhancements

- Cloud sync for settings across devices
- Backup compression for reduced storage usage
- Multiple Google account support
- Backup verification and integrity checks
- Advanced scheduling options (custom intervals)
