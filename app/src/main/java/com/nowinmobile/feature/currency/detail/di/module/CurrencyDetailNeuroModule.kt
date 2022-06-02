package com.nowinmobile.feature.currency.detail.di.module

import com.nowinmobile.feature.currency.detail.neuro.CurrencyDetailSignalHandler
import com.nowinmobile.lib.neuro.api.SignalHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object CurrencyDetailNeuroModule {
    @Provides
    @IntoSet
    fun provideCurrencyDetailSignalHandler(): SignalHandler {
        return CurrencyDetailSignalHandler()
    }
}
