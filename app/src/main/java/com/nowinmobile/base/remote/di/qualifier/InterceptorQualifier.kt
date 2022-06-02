package com.nowinmobile.base.remote.di.qualifier

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ContentTypeInterceptor

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CacheControlInterceptor
