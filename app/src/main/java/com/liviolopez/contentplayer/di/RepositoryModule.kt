package com.liviolopez.contentplayer.di

import com.liviolopez.contentplayer.data.local.AppDataBase
import com.liviolopez.contentplayer.data.local.AppDataStore
import com.liviolopez.contentplayer.data.remote.RemoteDataSource
import com.liviolopez.contentplayer.repository.Repository
import com.liviolopez.contentplayer.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        remoteData: RemoteDataSource,
        localData: AppDataBase,
        appDataStore: AppDataStore
    ): Repository {
        return RepositoryImpl(remoteData, localData, appDataStore)
    }

}