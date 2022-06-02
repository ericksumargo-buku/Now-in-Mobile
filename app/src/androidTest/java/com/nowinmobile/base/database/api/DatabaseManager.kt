package com.nowinmobile.base.database.api

import android.database.sqlite.SQLiteDatabase

interface DatabaseManager {
    /** Mock database for [table] with [values] and policy [conflictStrategy] for test fixture. */
    fun insert(
        table: String,
        values: Map<String, Any>,
        conflictStrategy: Int = SQLiteDatabase.CONFLICT_REPLACE,
    )
}
