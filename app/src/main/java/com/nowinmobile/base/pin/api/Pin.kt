package com.nowinmobile.base.pin.api

import kotlinx.coroutines.flow.Flow

interface Pin {
    /**
     * Observe value changes with [key] from preference file.
     * [defaultValue] will be the 1st emitted item if there's no such value associated with [key].
     */
    fun <T> observe(key: String, defaultValue: T): Flow<T>

    /**
     * Read value with [key] from preference file.
     * [defaultValue] will be returned if there's no such existing [key] or invalid cast data type.
     */
    suspend fun <T> read(key: String, defaultValue: T): T

    /** Save key-value in preference file. **/
    suspend fun <T> save(key: String, value: T): Boolean

    /** Remove all key-values from preference file. **/
    suspend fun clear(): Boolean
}
