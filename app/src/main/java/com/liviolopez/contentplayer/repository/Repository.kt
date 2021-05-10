package com.liviolopez.contentplayer.repository

import com.liviolopez.contentplayer.data.local.model.Item
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun initializeDb()

    fun filterItems(formats: List<String>, protection: String): Flow<List<Item>>
}