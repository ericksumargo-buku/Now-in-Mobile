package com.nowinmobile.data.app.implementation

import com.nowinmobile.base.coroutine.di.qualifier.IoDispatcher
import com.nowinmobile.base.pin.api.Pin
import com.nowinmobile.data.app.api.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAppRepository @Inject constructor(
    private val pin: Pin,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AppRepository {
    override suspend fun retrieveCurrency(): String {
        return withContext(context = ioDispatcher) {
            pin.read(key = CURRENCY_PREFERENCE, "")
        }
    }

    override suspend fun saveCurrency(code: String) {
        return withContext(context = ioDispatcher) {
            pin.save(key = CURRENCY_PREFERENCE, code)
        }
    }

    override suspend fun retrieveTheme(): Int {
        return withContext(context = ioDispatcher) {
            pin.read(key = THEME_PREFERENCE, 0)
        }
    }

    override suspend fun saveTheme(theme: Int) {
        return withContext(context = ioDispatcher) {
            pin.save(key = THEME_PREFERENCE, theme)
        }
    }

    private companion object {
        const val CURRENCY_PREFERENCE: String = "currency"

        const val THEME_PREFERENCE: String = "theme"
    }
}
