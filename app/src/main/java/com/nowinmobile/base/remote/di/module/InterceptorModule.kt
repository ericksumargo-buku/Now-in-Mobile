package com.nowinmobile.base.remote.di.module

import com.nowinmobile.base.remote.di.qualifier.CacheControlInterceptor
import com.nowinmobile.base.remote.di.qualifier.ContentTypeInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CacheControl
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import com.nowinmobile.base.remote.implementation.interceptor.CacheControlInterceptor as CacheControlInterceptorBinding
import com.nowinmobile.base.remote.implementation.interceptor.ContentTypeInterceptor as ContentTypeInterceptorBinding

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {
    @Provides
    @ContentTypeInterceptor
    fun provideContentTypeInterceptor(): Interceptor {
        return ContentTypeInterceptorBinding()
    }

    @Provides
    @CacheControlInterceptor
    fun provideCacheControlInterceptor(): Interceptor {
        val cacheControl = CacheControl.Builder().apply {
            maxAge(30, TimeUnit.MINUTES)
        }.build()
        return CacheControlInterceptorBinding(cacheControl)
    }
}
