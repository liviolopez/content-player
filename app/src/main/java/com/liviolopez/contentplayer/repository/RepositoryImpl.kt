package com.liviolopez.contentplayer.repository

import android.app.Application
import android.content.res.AssetManager.ACCESS_STREAMING
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liviolopez.contentplayer.data.local.AppDataBase
import com.liviolopez.contentplayer.data.local.AppDataStore
import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.data.remote.RemoteDataSource
import com.liviolopez.contentplayer.utils.syncNetworkResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

/** TODO()
 * remoteData, appDataStore
 */
class RepositoryImpl(
    private val remoteData: RemoteDataSource,
    private val localData: AppDataBase,
    private val appDataStore: AppDataStore,
    private val application: Application
) : Repository {
    private val itemDao = localData.itemDao()

    override suspend fun initializeDb(){
        syncSampleContentItems()
    }

    private suspend fun syncSampleContentItems() = syncNetworkResource(
        shouldFetch = {
            coroutineScope { itemDao.getCountItems() == 0 }
        },
        runFetchCall = {
            val itemsJson = application.assets.open("contentplayer.items.json", ACCESS_STREAMING).bufferedReader().readText()

            val items: List<Item> = try {
                Gson().fromJson(itemsJson, object: TypeToken<List<Item>>() {}.type)
            } catch (e: Throwable) {
                emptyList()
            }

            items
        },
        saveFetchResult = { items ->
            localData.withTransaction {
                itemDao.deleteAllItems()
                itemDao.insertItems(items)
            }
        }
    )

    override fun filterItems(formats: List<String>, protection: String): Flow<List<Item>> {
        val query = itemDao.createSimpleQuery(formats, protection)
        return itemDao.getItemRaw(query)
    }
}