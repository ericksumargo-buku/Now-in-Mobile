package com.nowinmobile.base.utils

import android.os.Bundle

fun Map<String, Any>.toBundle(): Bundle {
    val bundle = Bundle()
    for ((key, value) in this) {
        when (value) {
            is Boolean -> bundle.putBoolean(key, value)
            is Int -> bundle.putInt(key, value)
            is Long -> bundle.putLong(key, value)
            is Double -> bundle.putDouble(key, value)
            is String -> bundle.putString(key, value)
            is BooleanArray -> bundle.putBooleanArray(key, value)
            is IntArray -> bundle.putIntArray(key, value)
            is LongArray -> bundle.putLongArray(key, value)
            is DoubleArray -> bundle.putDoubleArray(key, value)
            else -> bundle.putString(key, "$value")
        }
    }
    return bundle
}
