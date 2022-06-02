package com.nowinmobile.base.database.di.module

import com.nowinmobile.base.database.api.DatabaseManager
import com.nowinmobile.base.database.implementation.DefaultDatabaseManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseManagerModule {
    @Binds
    fun DefaultDatabaseManager.bindDatabaseManager(): DatabaseManager
}
