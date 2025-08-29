package com.exparo.drivebackup.di

import android.content.Context
import androidx.room.Room
import com.exparo.drivebackup.data.BackupDatabase
import com.exparo.drivebackup.data.dao.BackupMetadataDao
import com.exparo.drivebackup.data.dao.ScheduledBackupSettingsDao
import com.exparo.drivebackup.data.repository.BackupMetadataRepository
import com.exparo.drivebackup.data.repository.ScheduledBackupSettingsRepository
import com.exparo.drivebackup.data.repository.ScheduledBackupRepository
import com.exparo.drivebackup.service.BackupManager
import com.exparo.drivebackup.service.GoogleDriveService
import com.exparo.drivebackup.service.UserNameUpdateService
import com.exparo.drivebackup.util.EncryptionUtil
import com.exparo.data.backup.BackupDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DriveBackupModule {
    
    @Provides
    @Singleton
    fun provideBackupDatabase(@ApplicationContext context: Context): BackupDatabase {
        return Room.databaseBuilder(
            context,
            BackupDatabase::class.java,
            "backup_database"
        ).build()
    }
    
    @Provides
    fun provideBackupMetadataDao(database: BackupDatabase): BackupMetadataDao {
        return database.backupMetadataDao()
    }
    
    @Provides
    fun provideScheduledBackupSettingsDao(database: BackupDatabase): ScheduledBackupSettingsDao {
        return database.scheduledBackupSettingsDao()
    }
    
    @Provides
    @Singleton
    fun provideBackupMetadataRepository(backupMetadataDao: BackupMetadataDao): BackupMetadataRepository {
        return BackupMetadataRepository(backupMetadataDao)
    }
    
    @Provides
    @Singleton
    fun provideScheduledBackupSettingsRepository(
        scheduledBackupSettingsDao: ScheduledBackupSettingsDao
    ): ScheduledBackupSettingsRepository {
        return ScheduledBackupSettingsRepository(scheduledBackupSettingsDao)
    }
    
    @Provides
    @Singleton
    fun provideScheduledBackupRepository(
        @ApplicationContext context: Context
    ): ScheduledBackupRepository {
        return ScheduledBackupRepository(context)
    }
    
    @Provides
    @Singleton
    fun provideBackupManager(
        @ApplicationContext context: Context,
        googleDriveService: GoogleDriveService,
        backupMetadataRepository: BackupMetadataRepository,
        backupDataUseCase: BackupDataUseCase,
        encryptionUtil: EncryptionUtil
    ): BackupManager {
        return BackupManager(context, googleDriveService, backupMetadataRepository, backupDataUseCase, encryptionUtil)
    }
    
    @Provides
    @Singleton
    fun provideGoogleDriveService(
        @ApplicationContext context: Context
    ): GoogleDriveService {
        return GoogleDriveService(context)
    }
    
    @Provides
    @Singleton
    fun provideEncryptionUtil(
        @ApplicationContext context: Context
    ): EncryptionUtil {
        return EncryptionUtil(context)
    }
    
    @Provides
    @Singleton
    fun provideUserNameUpdateService(
        @ApplicationContext context: Context,
        settingsDao: com.exparo.data.db.dao.read.SettingsDao,
        settingsWriter: com.exparo.data.db.dao.write.WriteSettingsDao
    ): UserNameUpdateService {
        return UserNameUpdateService(context, settingsDao, settingsWriter)
    }
}