# Scheduled Backup Troubleshooting Guide

## Issue Summary
User reported that scheduled backup set for 02:46 AM with Daily frequency did not trigger automatically at the specified time.

## Root Cause Analysis

### 1. **Android System Constraints**
- **Doze Mode**: Android puts apps in Doze mode during deep sleep, preventing background work
- **Battery Optimization**: Apps are battery-optimized by default, limiting background execution
- **WorkManager Limitations**: 15-minute minimum execution interval, inexact timing

### 2. **Missing Permissions**
Added the following critical permissions to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.USE_EXACT_ALARM" />
```

### 3. **Timing Logic Issues**
- **Edge Case**: When setting backup time very close to current time (4-minute difference)
- **WorkManager Minimum**: 15-minute minimum interval enforcement
- **Buffer Issue**: Original logic didn't account for edge cases

## Code Fixes Applied

### 1. **ScheduledBackupRepository.kt**
```kotlin
// Fixed timing calculation with buffer
if (calendar.timeInMillis <= (now + 60000)) { // Added 1-minute buffer
    calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
}

// Ensure minimum delay for WorkManager reliability
val finalInitialDelay = if (initialDelay < 900000) { // 15 minutes
    900000L // Set to 15 minutes minimum
} else {
    initialDelay
}
```

### 2. **Enhanced Constraints**
```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED) // Required for upload
    .setRequiresBatteryNotLow(false) // Allow on low battery
    .setRequiresCharging(false) // Allow without charging
    .setRequiresDeviceIdle(false) // Allow when active
    .setRequiresStorageNotLow(true) // Ensure storage available
    .build()
```

### 3. **Debug Functions Added**
- `getDetailedBackupStatus()`: Comprehensive status reporting
- `scheduleTestBackup()`: 2-minute test backup for debugging
- Enhanced logging throughout the workflow

## User Actions Required

### 1. **Battery Optimization Settings**
1. Go to **Settings > Apps > Exparo Wallet > Battery**
2. Select **"Don't optimize"** or **"Unrestricted"**
3. Confirm the change

### 2. **Alternative: Manual Testing**
1. Use the new **"Test Backup (2 min)"** button in Debug Tools
2. Monitor logs to verify execution
3. Check backup creation in Google Drive

### 3. **Recommended Schedule**
- **Avoid** scheduling backups less than 15 minutes in the future
- **Recommend** scheduling at least 30 minutes ahead for first setup
- **Use** common times like 2:00 AM, 3:00 AM (not 2:46 AM)

## Debug Tools Usage

### In Development Build
1. Navigate to **Scheduled Backup Settings**
2. Scroll to **Debug Tools** section
3. Use **"Test Backup (2 min)"** to test immediate scheduling
4. Use **"Check Status"** to view detailed WorkManager status
5. Monitor Android Studio logs with filter: `ScheduledBackup`

### Log Tags to Monitor
```
ScheduledBackup - Repository logs
ScheduledBackupWorker - Worker execution logs
ScheduledBackupViewModel - UI interaction logs
```

## Testing Procedure

### Immediate Test (Development)
1. Enable **Debug Tools** in scheduled backup settings
2. Tap **"Test Backup (2 min)"**
3. Wait 2 minutes and check if backup appears in Google Drive
4. Check logs for execution confirmation

### Real Schedule Test
1. Set backup time to **at least 30 minutes** from current time
2. Enable **Daily** frequency
3. Ensure **battery optimization is disabled**
4. Wait for scheduled time and verify backup creation

## Common Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| Backup not triggered | Battery optimization enabled | Disable battery optimization |
| Work gets cancelled | Doze mode restrictions | Add IGNORE_BATTERY_OPTIMIZATIONS permission |
| Inexact timing | WorkManager limitations | Set backup time with buffer (15+ min) |
| Network failure | No internet during backup | Backup will retry on next schedule |
| Google Drive auth | User not signed in | Re-authenticate with Google Drive |

## Future Improvements

1. **AlarmManager Integration**: For exact timing requirements
2. **Foreground Service**: For critical backup operations
3. **User Education**: Better UI guidance for battery optimization
4. **Backup Verification**: Post-backup integrity checks
5. **Retry Logic**: Exponential backoff for failed attempts

## Monitoring Commands

### ADB Commands for Testing
```bash
# Check battery optimization status
adb shell dumpsys deviceidle whitelist

# Force Doze mode for testing
adb shell dumpsys deviceidle force-idle

# Exit Doze mode
adb shell dumpsys deviceidle unforce

# Check WorkManager jobs
adb shell dumpsys jobscheduler
```

## Conclusion

The main issue was likely Android's aggressive battery optimization preventing the WorkManager from executing at the scheduled time. The fixes include proper permissions, improved timing logic, and debug tools to help verify functionality.

**Recommendation**: Test with the new debug tools first, then set up a real schedule with proper timing buffers.