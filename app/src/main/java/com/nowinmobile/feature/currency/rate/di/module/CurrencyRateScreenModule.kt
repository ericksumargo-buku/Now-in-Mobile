package com.nowinmobile.feature.currency.rate.di.module

import androidx.lifecycle.SavedStateHandle
import com.nowinmobile.base.data.api.Response
import com.nowinmobile.base.udf.api.state.StateManager
import com.nowinmobile.base.udf.implementation.state.DefaultStateManager
import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.feature.currency.rate.screen.CurrencyRateState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CurrencyRateScreenModule {
    @Provides
    fun provideCurrencyRateState(): CurrencyRateState {
        return CurrencyRateState(
            theme = 0,
            amount = -1.0,
            source = Currency(code = ""),
            errorMessage = null,
            reloadCurrencyRate = false,
            currencyResponse = Response.Empty,
            currencyRateResponse = listOf(),
        )
    }

    @Provides
    fun provideStateManager(
        initState: CurrencyRateState,
        savedStateHandle: SavedStateHandle,
    ): StateManager<CurrencyRateState> {
        return DefaultStateManager(initState, savedStateHandle)
    }
}
