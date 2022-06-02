package com.nowinmobile.feature.currency.detail.screen

import com.nowinmobile.base.udf.api.screen.UdfViewModel
import com.nowinmobile.base.udf.api.state.StateManager
import com.nowinmobile.data.currency.api.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailViewModel @Inject constructor(
    stateManager: StateManager<CurrencyDetailState>
) : UdfViewModel<CurrencyDetailState>(stateManager) {
    fun updateSource(code: String) {
        produce { state ->
            state.copy(source = Currency(code))
        }
    }
}
