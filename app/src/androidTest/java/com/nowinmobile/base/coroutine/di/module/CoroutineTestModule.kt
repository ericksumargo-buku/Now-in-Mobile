package com.nowinmobile.base.coroutine.di.module

import android.os.AsyncTask.THREAD_POOL_EXECUTOR
import com.nowinmobile.base.coroutine.di.qualifier.DefaultDispatcher
import com.nowinmobile.base.coroutine.di.qualifier.IoDispatcher
import com.nowinmobile.base.coroutine.di.qualifier.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoroutineModule::class]
)
object CoroutineTestModule {
    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = THREAD_POOL_EXECUTOR.asCoroutineDispatcher()

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
}
