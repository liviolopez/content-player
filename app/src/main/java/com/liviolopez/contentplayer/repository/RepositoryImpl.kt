package com.liviolopez.contentplayer.repository

import androidx.room.withTransaction
import com.liviolopez.contentplayer.data.local.AppDataBase
import com.liviolopez.contentplayer.data.local.AppDataStore
import com.liviolopez.contentplayer.data.remote.RemoteDataSource
import com.liviolopez.contentplayer.data.remote.response.toLocalModel
import com.liviolopez.contentplayer.utils.networkBoundResource
import com.liviolopez.contentplayer.utils.syncNetworkResource
import kotlinx.coroutines.coroutineScope

class RepositoryImpl(
    private val remoteData: RemoteDataSource,
    private val localData: AppDataBase,
    private val appDataStore: AppDataStore
) : Repository {
    private val itemDao = localData.itemDao()

    // Fetch data from the API each 10min (isDbUpdateNeeded())
    override suspend fun initializeCache() = syncNetworkResource(
        shouldFetch = {
            coroutineScope {
                appDataStore.isDbUpdateNeeded() || itemDao.getCountItems() == 0
            }
        },
        runFetchCall = {
            remoteData.fetchItems()
        },
        saveFetchResult = { items ->
            localData.withTransaction {
                itemDao.deleteAllItems()
                itemDao.insertItems(items.map { it.toLocalModel() })
                appDataStore.registerDbUpdate()
            }
        }
    )

    // Always fetch all items in the API
    override fun fetchItems() = networkBoundResource(
        loadFromDb = {
            itemDao.getAllItems()
        },
        createCall = {
            remoteData.fetchItems()
        },
        saveFetchResult = { items ->
            localData.withTransaction {
                itemDao.deleteAllItems()
                itemDao.insertItems(items.map { it.toLocalModel() })
                appDataStore.registerDbUpdate()
            }
        }
    )
}