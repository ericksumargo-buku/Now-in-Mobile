package com.nowinmobile.base.remote.di.module

import com.nowinmobile.base.remote.api.RemoteServer
import com.nowinmobile.base.remote.implementation.DefaultRemoteServer
import com.nowinmobile.base.remote.implementation.interceptor.ResponseInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import com.nowinmobile.base.remote.di.qualififer.ResponseInterceptor as ResponseInterceptorQualifier

@Module
@InstallIn(SingletonComponent::class)
interface RemoteServerModule {
    @Binds
    fun DefaultRemoteServer.bindRemoteServer(): RemoteServer

    @Binds
    @ResponseInterceptorQualifier
    fun ResponseInterceptor.bindResponseInterceptor(): Interceptor
}
