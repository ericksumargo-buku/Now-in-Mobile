package com.nowinmobile.lib.neuro.api

import android.content.Context

data class Signal(
    val context: Context,
    val scheme: String,
    val host: String,
    val parameter: Map<String, Any>,
    val query: Map<String, Any>,
)