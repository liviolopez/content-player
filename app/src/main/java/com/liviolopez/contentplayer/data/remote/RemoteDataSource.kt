package com.liviolopez.contentplayer.data.remote

import com.liviolopez.contentplayer.data.remote.response.ItemDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun fetchItems(): List<ItemDto> {
        return apiService.getItems()
    }
}