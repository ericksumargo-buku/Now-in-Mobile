package com.nowinmobile.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nowinmobile.data.currency.implementation.room.api.CurrencyDao
import com.nowinmobile.data.currency.implementation.room.api.entity.CurrencyEntity

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
}
