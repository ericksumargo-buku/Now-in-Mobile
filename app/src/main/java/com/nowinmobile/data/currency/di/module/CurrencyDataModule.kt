package com.nowinmobile.data.currency.di.module

import com.nowinmobile.data.currency.api.CurrencyRepository
import com.nowinmobile.data.currency.implementation.DefaultCurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyDataModule {
    @Binds
    fun DefaultCurrencyRepository.bindCurrencyRepository(): CurrencyRepository
}
