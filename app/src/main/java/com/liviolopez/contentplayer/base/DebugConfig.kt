package com.liviolopez.contentplayer.base

import okhttp3.logging.HttpLoggingInterceptor

object DebugConfig {
    val Retrofit = HttpLoggingInterceptor.Level.BASIC // NONE BASIC HEADERS BODY

    object Room {
        const val showQueries = false // TRUE FALSE :: Show DB queries and params in Android Studio's logcat
    }
}