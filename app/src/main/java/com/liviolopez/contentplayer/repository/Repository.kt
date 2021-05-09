package com.liviolopez.contentplayer.repository

import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun initializeCache()

    fun fetchItems(): Flow<Resource<List<Item>>>
}