package com.liviolopez.contentplayer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteParamsModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return "http://127.0.0.1:8080"
    }

}