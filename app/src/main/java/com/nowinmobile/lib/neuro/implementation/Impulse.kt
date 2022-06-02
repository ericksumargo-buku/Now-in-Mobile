package com.nowinmobile.lib.neuro.implementation

import android.content.Context

internal data class Impulse(
    val context: Context,
    val scheme: String,
    val host: String,
    val paths: List<String>,
    val query: Map<String, Any>,
)