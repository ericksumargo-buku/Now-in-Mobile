package com.nowinmobile.data.currency.api

import com.nowinmobile.base.data.api.Response
import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.data.currency.api.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun fetchCurrency(refresh: Boolean): Flow<Response<List<Currency>>>

    fun fetchCurrencyRates(
        amount: Double,
        source: Currency,
        page: Int,
        limit: Int,
    ): Flow<Response<List<CurrencyRate>>>
}
