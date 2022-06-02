package com.nowinmobile.base.messenger.di.module

import com.nowinmobile.base.messenger.api.MessageRecorder
import com.nowinmobile.base.messenger.api.Messenger
import com.nowinmobile.base.messenger.implementation.DefaultMessageRecorder
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MessengerModule::class],
)
interface MessengerTestModule {
    @Binds
    fun DefaultMessageRecorder.bindMessageRecorder(): MessageRecorder

    @Binds
    fun MessageRecorder.bindMessenger(): Messenger
}
