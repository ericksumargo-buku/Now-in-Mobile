package com.nowinmobile.base.pin.di.module

import android.content.Context
import com.nowinmobile.base.coroutine.di.qualifier.IoDispatcher
import com.nowinmobile.base.pin.api.Pin
import com.nowinmobile.base.pin.implementation.DataStorePin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PinModule {
    @Provides
    @Singleton
    fun providePin(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): Pin {
        return DataStorePin(
            name = context.packageName,
            context = context,
            ioDispatcher = ioDispatcher,
        )
    }
}
