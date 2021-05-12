package com.liviolopez.contentplayer.di

import android.app.Application
import com.liviolopez.contentplayer.utils.DeviceInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Singleton
    @Provides
    fun provideDeviceInfo(app: Application) = DeviceInfo(app)

}