package com.exparo.base.di

import com.exparo.base.resource.AndroidResourceProvider
import com.exparo.base.resource.ResourceProvider
import com.exparo.base.threading.DispatchersProvider
import com.exparo.base.threading.ExparoDispatchersProvider
import com.exparo.base.time.TimeConverter
import com.exparo.base.time.TimeProvider
import com.exparo.base.time.impl.DeviceTimeProvider
import com.exparo.base.time.impl.StandardTimeConverter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BaseHiltBindings {
    @Binds
    fun dispatchersProvider(impl: ExparoDispatchersProvider): DispatchersProvider

    @Binds
    fun bindTimezoneProvider(impl: DeviceTimeProvider): TimeProvider

    @Binds
    fun bindTimeConverter(impl: StandardTimeConverter): TimeConverter

    @Binds
    fun resourceProvider(impl: AndroidResourceProvider): ResourceProvider
}