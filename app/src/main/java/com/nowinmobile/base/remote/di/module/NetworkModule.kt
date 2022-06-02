package com.nowinmobile.base.remote.di.module

import com.nowinmobile.base.remote.api.NetworkMonitor
import com.nowinmobile.base.remote.implementation.DefaultNetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    fun DefaultNetworkMonitor.bindNetworkMonitor(): NetworkMonitor
}
