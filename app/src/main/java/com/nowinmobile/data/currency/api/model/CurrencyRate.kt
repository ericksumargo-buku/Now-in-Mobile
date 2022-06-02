package com.nowinmobile.data.currency.api.model

import java.io.Serializable

data class CurrencyRate(
    val amount: Double,
    val source: Currency,
    val target: Currency,
    val rate: Double,
) : Serializable
