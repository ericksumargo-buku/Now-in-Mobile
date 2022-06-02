package com.nowinmobile.data.currency.implementation.remote.api

import com.nowinmobile.BuildConfig
import com.nowinmobile.data.currency.implementation.remote.api.response.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyRemote {
    @GET("/live?access_key=${BuildConfig.API_KEY}")
    suspend fun fetchCurrencyRate(): Response<CurrencyRateResponse>
}
