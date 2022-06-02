package com.nowinmobile.lib.neuro.implementation

import android.net.Uri

internal data class UriPart(
    val scheme: String?,
    val host: String?,
    val path: String?,
    val query: String?,
)

internal interface UriParser {
    operator fun invoke(url: String): UriPart
}

internal class DefaultUriParser : UriParser {
    override operator fun invoke(url: String): UriPart {
        val uri = Uri.parse(url)
        return UriPart(
            scheme = uri.scheme,
            host = uri.host,
            path = uri.path,
            query = uri.query,
        )
    }
}