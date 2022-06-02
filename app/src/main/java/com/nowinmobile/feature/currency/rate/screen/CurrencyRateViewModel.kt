package com.nowinmobile.feature.currency.rate.screen

import androidx.lifecycle.viewModelScope
import com.nowinmobile.base.data.api.Response
import com.nowinmobile.base.data.api.findSuccess
import com.nowinmobile.base.udf.api.screen.UdfViewModel
import com.nowinmobile.base.udf.api.state.StateManager
import com.nowinmobile.data.app.api.AppRepository
import com.nowinmobile.data.currency.api.CurrencyRepository
import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.data.currency.api.model.CurrencyRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyRateViewModel @Inject constructor(
    stateManager: StateManager<CurrencyRateState>,
    private val appRepository: AppRepository,
    private val currencyRepository: CurrencyRepository,
) : UdfViewModel<CurrencyRateState>(stateManager) {
    private var fetchCurrencyJob: Job? = null

    private var fetchCurrencyRatesJob: Job? = null

    fun fetchCurrency(refresh: Boolean) {
        fetchCurrencyJob?.cancel()
        fetchCurrencyJob = currencyRepository.fetchCurrency(refresh)
            .onEach(::handleResponse)
            .launchIn(viewModelScope)
    }

    fun fetchCurrencyRates(amount: Double, code: String, page: Int, limit: Int) {
        fetchCurrencyRatesJob?.cancel()
        fetchCurrencyRatesJob = currencyRepository.fetchCurrencyRates(
            amount,
            source = Currency(code),
            page,
            limit,
        ).onEach { response ->
            handleResponse(response, page)
        }.launchIn(viewModelScope)
    }

    fun fetchSourceCurrency() {
        viewModelScope.launch {
            val code = appRepository.retrieveCurrency()
            changeCurrency(code)
        }
    }

    fun fetchTheme() {
        viewModelScope.launch {
            val theme = appRepository.retrieveTheme()
            changeTheme(theme)
        }
    }

    fun changeTheme(theme: Int) {
        viewModelScope.launch {
            appRepository.saveTheme(theme)
        }

        produce { state ->
            state.copy(theme = theme)
        }
    }

    fun updateAmount(amount: Double) {
        produce { state ->
            state.copy(amount = amount)
        }
    }

    fun changeCurrency(code: String) {
        viewModelScope.launch {
            appRepository.saveCurrency(code)
        }

        produce { state ->
            state.copy(source = Currency(code))
        }
    }

    fun clearCurrencyRate() {
        produce { state ->
            state.copy(currencyRateResponse = listOf())
        }
    }

    fun clearErrorMessage() {
        produce { state ->
            state.copy(errorMessage = null)
        }
    }

    fun clearReloadRate() {
        produce { state ->
            state.copy(reloadCurrencyRate = false)
        }
    }

    private fun handleResponse(response: Response<List<Currency>>) {
        produce { state ->
            when (response) {
                is Response.Loading -> {
                    state.copy(currencyResponse = response)
                }
                is Response.Error -> {
                    state.copy(
                        errorMessage = response.message,
                        currencyResponse = response,
                    )
                }
                is Response.Empty -> {
                    state.copy(currencyResponse = response)
                }
                is Response.Success -> {
                    state.copy(
                        reloadCurrencyRate = true,
                        currencyResponse = response,
                    )
                }
            }
        }
    }

    private fun handleResponse(response: Response<List<CurrencyRate>>, page: Int) {
        produce { state ->
            when (response) {
                is Response.Loading, is Response.Error, is Response.Empty -> state.copy(
                    currencyRateResponse = listOf(response).takeIf {
                        page == 1
                    } ?: state.currencyRateResponse.dropLastWhile {
                        it is Response.Loading || it is Response.Error || it is Response.Empty
                    } + response
                )
                is Response.Success -> state.copy(
                    currencyRateResponse = listOf(response).takeIf {
                        page == 1
                    } ?: listOf(
                        Response.Success(
                            data = state.currencyRateResponse.findSuccess()?.data.orEmpty() + response.data
                        )
                    )
                )
            }
        }
    }
}
