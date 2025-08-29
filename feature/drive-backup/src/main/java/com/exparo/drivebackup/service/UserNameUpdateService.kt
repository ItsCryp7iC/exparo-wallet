package com.exparo.drivebackup.service

import android.content.Context
import com.exparo.data.db.dao.read.SettingsDao
import com.exparo.data.db.dao.write.WriteSettingsDao
import com.exparo.legacy.utils.ioThread
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserNameUpdateService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsDao: SettingsDao,
    private val settingsWriter: WriteSettingsDao
) {
    
    suspend fun updateUserNameFromGoogleAccount(googleDriveService: GoogleDriveService) {
        val googleUserName = googleDriveService.getCurrentUserName()
        
        if (!googleUserName.isNullOrBlank()) {
            ioThread {
                val currentSettings = settingsDao.findFirst()
                if (currentSettings.name.isNullOrBlank() || currentSettings.name == "Anonymous") {
                    settingsWriter.save(currentSettings.copy(name = googleUserName))
                }
            }
        }
    }
}
