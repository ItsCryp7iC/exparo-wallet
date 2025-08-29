package com.exparo.wallet

import android.app.Application
import androidx.work.Configuration
import com.exparo.base.legacy.appContext
import com.exparo.drivebackup.worker.ScheduledBackupWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

/**
 * Created by iliyan on 24.02.18.
 */
@HiltAndroidApp
class ExparoAndroidApp : Application(), Configuration.Provider {
    @Inject
    lateinit var scheduledBackupWorkerFactory: ScheduledBackupWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(scheduledBackupWorkerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        appContext = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
