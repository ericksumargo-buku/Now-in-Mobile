package com.nowinmobile.base.neuro.di.module

import com.nowinmobile.lib.neuro.api.Neuro
import com.nowinmobile.lib.neuro.implementation.DefaultNeuro
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NeuroModule {
    @Provides
    @Singleton
    fun provideNeuro(): Neuro = DefaultNeuro()
}
