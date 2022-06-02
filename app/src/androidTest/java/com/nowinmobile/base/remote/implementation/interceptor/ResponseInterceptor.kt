package com.nowinmobile.base.remote.implementation.interceptor

import com.nowinmobile.base.remote.api.RemoteServer
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseInterceptor @Inject constructor(
    private val remoteServer: RemoteServer,
) : Interceptor {
    private val shifts: MutableMap<String, Int> = mutableMapOf()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val method = request.method
        val path = request.url.toUri().path
        val key = "$method+$path"

        val pool = remoteServer.responsePool[key].orEmpty()
        if (pool.isEmpty()) {
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .code(503)
                .message("Service Unavailable")
                .body("".toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        var i = shifts[key] ?: 0
        if (i >= pool.size) i = pool.size - 1
        shifts[key] = i + 1

        val response = pool[i]
        val body = response.body
        val code = response.code

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .code(code)
            .message(body)
            .body(body.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}
