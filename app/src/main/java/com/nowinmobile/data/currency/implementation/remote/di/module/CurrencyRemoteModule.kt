package com.nowinmobile.data.currency.implementation.remote.di.module

import com.nowinmobile.data.currency.implementation.remote.api.CurrencyRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrencyRemoteModule {
    @Provides
    @Singleton
    fun provideCurrencyRemote(retrofit: Retrofit): CurrencyRemote {
        return retrofit.create(CurrencyRemote::class.java)
    }
}
