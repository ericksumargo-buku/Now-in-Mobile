package com.nowinmobile.data.app.api

interface AppRepository {
    suspend fun retrieveCurrency(): String

    suspend fun saveCurrency(code: String)

    suspend fun retrieveTheme(): Int

    suspend fun saveTheme(theme: Int)
}
