package com.nowinmobile.feature.currency.detail.di.module

import androidx.lifecycle.SavedStateHandle
import com.nowinmobile.base.udf.api.state.StateManager
import com.nowinmobile.base.udf.implementation.state.DefaultStateManager
import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.feature.currency.detail.screen.CurrencyDetailState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CurrencyDetailScreenModule {
    @Provides
    fun provideCurrencyDetailState(): CurrencyDetailState {
        return CurrencyDetailState(source = Currency(""))
    }

    @Provides
    fun provideStateManager(
        initState: CurrencyDetailState,
        savedStateHandle: SavedStateHandle,
    ): StateManager<CurrencyDetailState> {
        return DefaultStateManager(initState, savedStateHandle)
    }
}
