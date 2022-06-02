package com.nowinmobile.base.data.api

import java.io.Serializable

sealed interface Response<out T> : Serializable {
    object Loading : Response<Nothing>

    open class Error(
        val message: String,
        val meta: Map<String, Any?> = mapOf(),
    ) : Response<Nothing>

    object Empty : Response<Nothing>

    class Success<T>(
        val data: T,
        val meta: Map<String, Any?> = mapOf(),
    ) : Response<T>
}

fun <T> List<Response<T>>.findSuccess(): Response.Success<T>? {
    return firstOrNull { it is Response.Success } as? Response.Success
}
