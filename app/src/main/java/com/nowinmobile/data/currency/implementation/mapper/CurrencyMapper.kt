package com.nowinmobile.data.currency.implementation.mapper

import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.data.currency.implementation.room.api.entity.CurrencyEntity

fun CurrencyEntity.toCurrency(): Currency {
    return Currency(code = id)
}
