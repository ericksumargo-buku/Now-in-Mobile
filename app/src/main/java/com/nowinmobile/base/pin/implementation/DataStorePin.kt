package com.nowinmobile.base.pin.implementation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.nowinmobile.base.pin.api.Pin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStorePin(
    private val name: String,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : Pin {
    private var coroutineScope = createCoroutineScope()

    private var dataStore: DataStore<Preferences> = createDataStore()

    private fun createCoroutineScope(): CoroutineScope {
        return CoroutineScope(ioDispatcher + SupervisorJob())
    }

    private fun createDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = coroutineScope,
            produceFile = { context.preferencesDataStoreFile(name) }
        )
    }

    override fun <T> observe(key: String, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences.retrieve(key, defaultValue)
        }
    }

    override suspend fun <T> read(key: String, defaultValue: T): T {
        return observe(key, defaultValue).first()
    }

    private fun <T> Preferences.retrieve(key: String, defaultValue: T): T {
        return try {
            when (defaultValue) {
                is Int -> {
                    val value = get(intPreferencesKey(key))
                    value.takeIf { it is Int } ?: defaultValue
                }
                is Double -> {
                    val value = get(doublePreferencesKey(key))
                    value.takeIf { it is Double } ?: defaultValue
                }
                is Boolean -> {
                    val value = get(booleanPreferencesKey(key))
                    value.takeIf { it is Boolean } ?: defaultValue
                }
                is Float -> {
                    val value = get(floatPreferencesKey(key))
                    value.takeIf { it is Float } ?: defaultValue
                }
                is Long -> {
                    val value = get(longPreferencesKey(key))
                    value.takeIf { it is Long } ?: defaultValue
                }
                else -> {
                    val value = get(stringPreferencesKey(key))
                    value.takeIf { it is String } ?: defaultValue
                }
            } as T
        } catch (cause: Exception) {
            defaultValue
        }
    }

    override suspend fun <T> save(key: String, value: T): Boolean {
        return try {
            dataStore.edit { preferences ->
                when (value) {
                    is Int -> preferences[intPreferencesKey(key)] = value
                    is Double -> preferences[doublePreferencesKey(key)] = value
                    is Boolean -> preferences[booleanPreferencesKey(key)] = value
                    is Float -> preferences[floatPreferencesKey(key)] = value
                    is Long -> preferences[longPreferencesKey(key)] = value
                    else -> preferences[stringPreferencesKey(key)] = "$value"
                }
            }
            true
        } catch (cause: Exception) {
            false
        }
    }

    override suspend fun clear(): Boolean {
        return try {
            coroutineScope.cancel()
            context.preferencesDataStoreFile(name).deleteRecursively()

            reset()
            true
        } catch (cause: Exception) {
            false
        }
    }

    private fun reset() {
        coroutineScope = createCoroutineScope()
        dataStore = createDataStore()
    }
}
