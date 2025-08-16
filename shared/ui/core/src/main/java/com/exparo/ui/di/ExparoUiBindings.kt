package com.exparo.ui.di

import com.exparo.ui.time.DevicePreferences
import com.exparo.ui.time.TimeFormatter
import com.exparo.ui.time.impl.AndroidDateTimePicker
import com.exparo.ui.time.impl.AndroidDevicePreferences
import com.exparo.ui.time.impl.DateTimePicker
import com.exparo.ui.time.impl.ExparoTimeFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ExparoUiBindings {
    @Binds
    fun timeFormatter(impl: ExparoTimeFormatter): TimeFormatter

    @Binds
    fun deviceTimePreferences(impl: AndroidDevicePreferences): DevicePreferences

    @Binds
    fun dateTimePicker(impl: AndroidDateTimePicker): DateTimePicker
}