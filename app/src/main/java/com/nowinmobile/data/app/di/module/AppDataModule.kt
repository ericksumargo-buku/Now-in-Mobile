package com.nowinmobile.data.app.di.module

import com.nowinmobile.data.app.api.AppRepository
import com.nowinmobile.data.app.implementation.DefaultAppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppDataModule {
    @Binds
    fun DefaultAppRepository.bindAppRepository(): AppRepository
}
