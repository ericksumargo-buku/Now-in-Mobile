package com.nowinmobile.base.database.di.module

import com.nowinmobile.base.database.AppDatabase
import com.nowinmobile.data.currency.implementation.room.api.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideCurrencyDao(database: AppDatabase): CurrencyDao {
        return database.currencyDao
    }
}
