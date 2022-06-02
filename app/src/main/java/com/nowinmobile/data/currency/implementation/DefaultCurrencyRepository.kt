package com.nowinmobile.data.currency.implementation

import com.nowinmobile.base.coroutine.di.qualifier.IoDispatcher
import com.nowinmobile.base.data.api.Response
import com.nowinmobile.data.currency.api.CurrencyRepository
import com.nowinmobile.data.currency.api.model.Currency
import com.nowinmobile.data.currency.api.model.CurrencyRate
import com.nowinmobile.data.currency.implementation.mapper.toCurrency
import com.nowinmobile.data.currency.implementation.remote.api.CurrencyRemote
import com.nowinmobile.data.currency.implementation.room.api.CurrencyDao
import com.nowinmobile.data.currency.implementation.room.api.entity.CurrencyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val currencyRemote: CurrencyRemote,
    private val currencyDao: CurrencyDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CurrencyRepository {
    override fun fetchCurrency(refresh: Boolean): Flow<Response<List<Currency>>> = flow {
        emit(Response.Loading)

        if (!refresh) {
            val currency = retrieveCurrency()
            if (currency.isNotEmpty()) {
                emit(Response.Success(data = currency))
                return@flow
            }
        }

        val response = currencyRemote.fetchCurrencyRate()
        val body = response.body()
        val error = body?.error

        if (!response.isSuccessful) {
            emit(Response.Error(message = response.message().orEmpty()))
        } else if (error != null) {
            emit(
                Response.Error(
                    message = error.info,
                    meta = mapOf(
                        "code" to error.code,
                        "type" to error.type,
                    )
                )
            )
        } else if (body != null) {
            saveCurrency(quotes = body.quotes.orEmpty())

            val currency = retrieveCurrency()
            emit(Response.Success(data = currency))
        } else {
            emit(Response.Empty)
        }
    }.flowOn(context = ioDispatcher)

    override fun fetchCurrencyRates(
        amount: Double,
        source: Currency,
        page: Int,
        limit: Int,
    ): Flow<Response<List<CurrencyRate>>> = flow {
        emit(Response.Loading)

        val selected = currencyDao.selectCurrency(id = source.code)
        val records = currencyDao.selectCurrency(page, limit)

        if (selected == null || records.isEmpty()) {
            emit(Response.Empty)
        } else {
            emit(
                Response.Success(
                    data = records.map { record ->
                        CurrencyRate(
                            amount = amount,
                            source = selected.toCurrency(),
                            target = record.toCurrency(),
                            rate = convertRate(amount, selected.rate, record.rate)
                        )
                    },
                )
            )
        }
    }.flowOn(context = ioDispatcher)

    private suspend fun saveCurrency(quotes: Map<String, Double>) {
        currencyDao.insertCurrency(
            currency = quotes.map { (code, rate) ->
                CurrencyEntity(code.drop(3), rate)
            }
        )
    }

    private suspend fun retrieveCurrency(): List<Currency> {
        return currencyDao.selectCurrency().map(CurrencyEntity::toCurrency)
    }

    private fun convertRate(
        amount: Double,
        source: Double,
        target: Double,
    ): Double {
        return amount * (source / target)
    }
}
