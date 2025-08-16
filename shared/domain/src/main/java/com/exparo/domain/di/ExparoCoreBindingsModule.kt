package com.exparo.domain.di

import com.exparo.domain.features.Features
import com.exparo.domain.features.ExparoFeatures
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ExparoCoreBindingsModule {
    @Binds
    fun bindFeatures(features: ExparoFeatures): Features
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FeaturesEntryPoint {
    fun getFeatures(): Features
}
