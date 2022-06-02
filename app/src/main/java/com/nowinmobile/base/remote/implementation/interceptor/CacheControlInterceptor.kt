package com.nowinmobile.base.remote.implementation.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class CacheControlInterceptor(private val cacheControl: CacheControl) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
            .newBuilder()
            .cacheControl(cacheControl)
            .build()
        return chain.proceed(request)
    }
}
