package com.liviolopez.contentplayer.data.remote

import com.liviolopez.contentplayer.data.remote.response.ApiItemsResponse
import retrofit2.http.GET

// https://liviolopez.com/api/content-player

interface ApiService {
    @GET("api/content-player")
    suspend fun getItems(): ApiItemsResponse
}