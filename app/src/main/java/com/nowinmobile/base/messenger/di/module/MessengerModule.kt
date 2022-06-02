package com.nowinmobile.base.messenger.di.module

import com.nowinmobile.base.messenger.api.Messenger
import com.nowinmobile.base.messenger.implementation.DefaultMessenger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface MessengerModule {
    @Binds
    fun DefaultMessenger.bindMessenger(): Messenger
}
