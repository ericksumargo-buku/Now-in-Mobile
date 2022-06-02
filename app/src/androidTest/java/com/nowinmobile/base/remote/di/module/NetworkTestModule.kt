package com.nowinmobile.base.remote.di.module

import com.nowinmobile.base.remote.api.NetworkMonitor
import com.nowinmobile.base.remote.api.WritableNetworkMonitor
import com.nowinmobile.base.remote.implementation.DefaultWritableNetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class],
)
interface NetworkTestModule {
    @Binds
    fun DefaultWritableNetworkMonitor.bindWritableNetworkMonitor(): WritableNetworkMonitor

    @Binds
    fun WritableNetworkMonitor.bindNetworkMonitor(): NetworkMonitor
}
