package com.nowinmobile.base.remote.implementation.interceptor

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class ContentTypeInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
            .newBuilder()
            .header("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}
