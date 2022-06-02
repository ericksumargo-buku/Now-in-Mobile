package com.nowinmobile.base.database.implementation

import android.content.ContentValues
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.nowinmobile.base.database.api.DatabaseManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultDatabaseManager @Inject constructor(
    private val sqliteHelper: SupportSQLiteOpenHelper,
) : DatabaseManager {
    override fun insert(table: String, values: Map<String, Any>, conflictStrategy: Int) {
        val contentValues = ContentValues()
        for ((key, value) in values) {
            when (value) {
                is Boolean -> contentValues.put(key, value)
                is Byte -> contentValues.put(key, value)
                is Short -> contentValues.put(key, value)
                is Int -> contentValues.put(key, value)
                is Long -> contentValues.put(key, value)
                is Float -> contentValues.put(key, value)
                is Double -> contentValues.put(key, value)
                is String -> contentValues.put(key, value)
                else -> contentValues.put(key, "$value")
            }
        }
        sqliteHelper.readableDatabase.insert(table, conflictStrategy, contentValues)
    }
}
