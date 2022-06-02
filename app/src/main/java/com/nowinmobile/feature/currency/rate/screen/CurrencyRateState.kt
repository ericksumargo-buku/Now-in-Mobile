package com.nowinmobile.feature.currency.rate.screen

import com.nowinmobile.base.data.api.Response
import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.data.currency.api.model.CurrencyRate
import java.io.Serializable

data class CurrencyRateState(
    val theme: Int,
    val amount: Double,
    val source: Currency,
    val errorMessage: String?,
    val reloadCurrencyRate: Boolean,
    val currencyResponse: Response<List<Currency>>,
    val currencyRateResponse: List<Response<List<CurrencyRate>>>,
) : Serializable
