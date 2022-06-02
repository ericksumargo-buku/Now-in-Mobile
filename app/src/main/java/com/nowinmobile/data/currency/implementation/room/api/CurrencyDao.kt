package com.nowinmobile.data.currency.implementation.room.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nowinmobile.data.currency.implementation.room.api.entity.CurrencyEntity

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: List<CurrencyEntity>): LongArray

    @Query("SELECT * FROM currency")
    suspend fun selectCurrency(): List<CurrencyEntity>

    @Query("SELECT * FROM currency LIMIT :limit OFFSET (:page - 1) * :limit")
    suspend fun selectCurrency(page: Int, limit: Int): List<CurrencyEntity>

    @Query("SELECT * FROM currency WHERE id = :id")
    suspend fun selectCurrency(id: String): CurrencyEntity?
}
