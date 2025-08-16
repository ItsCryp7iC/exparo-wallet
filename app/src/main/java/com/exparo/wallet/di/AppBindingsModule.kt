package com.exparo.wallet.di

import com.exparo.domain.AppStarter
import com.exparo.wallet.ExparoAppStarter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingsModule {
    @Binds
    abstract fun appStarter(appStarter: ExparoAppStarter): AppStarter
}
