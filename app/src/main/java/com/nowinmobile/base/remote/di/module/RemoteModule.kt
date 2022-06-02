package com.nowinmobile.base.remote.di.module

import android.content.Context
import com.nowinmobile.base.remote.di.qualifier.CacheControlInterceptor
import com.nowinmobile.base.remote.di.qualifier.ContentTypeInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        @ContentTypeInterceptor contentTypeInterceptor: Interceptor,
        @CacheControlInterceptor cacheControlInterceptor: Interceptor,
    ): OkHttpClient {
        val cache = Cache(context.cacheDir, 5 * 1024 * 1024)

        return OkHttpClient.Builder()
            .addInterceptor(contentTypeInterceptor)
            .addNetworkInterceptor(cacheControlInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.currencylayer.com")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}
