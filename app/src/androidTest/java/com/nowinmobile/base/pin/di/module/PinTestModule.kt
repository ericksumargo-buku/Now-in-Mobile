package com.nowinmobile.base.pin.di.module

import android.content.Context
import com.nowinmobile.base.coroutine.di.qualifier.IoDispatcher
import com.nowinmobile.base.pin.api.Pin
import com.nowinmobile.base.pin.implementation.DataStorePin
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PinModule::class],
)
object PinTestModule {
    @Provides
    @Singleton
    fun provideTestPin(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): Pin {
        return DataStorePin(
            name = "test",
            context = context,
            ioDispatcher = ioDispatcher,
        )
    }
}
