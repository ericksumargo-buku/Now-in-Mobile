package com.nowinmobile.base.neuro.api

import android.content.Context
import com.nowinmobile.lib.neuro.api.Neuro
import com.nowinmobile.lib.neuro.api.SourceLink

const val INTERNAL_SCHEME = "internal"

const val INTERNAL_HOST = "launch"

fun Neuro.navigate(
    context: Context,
    path: String,
    query: Map<String, Any> = mapOf(),
    onSuccess: () -> Unit = {},
    onFailure: (cause: Throwable) -> Unit = {},
) {
    val normalizedPath = path.trim().run {
        if (isEmpty()) return
        if (first() == '/') drop(1)
        else this
    }

    val queryBuilder = StringBuilder()
    for ((key, value) in query) {
        if (queryBuilder.isEmpty()) queryBuilder.append('?')
        else queryBuilder.append('&')

        queryBuilder.append("$key=$value")
    }

    val link = SourceLink(
        context = context,
        link = "$INTERNAL_SCHEME://$INTERNAL_HOST/$normalizedPath$queryBuilder"
    )
    direct(link, onSuccess, onFailure)
}
