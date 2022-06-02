package com.nowinmobile.data.currency.implementation.remote.api.response

import com.google.gson.annotations.SerializedName

data class CurrencyRateResponse(
    @SerializedName("quotes")
    val quotes: Map<String, Double>?,
    @SerializedName("error")
    val error: Error?,
) {
    data class Error(
        @SerializedName("code")
        val code: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("info")
        val info: String,
    )
}
