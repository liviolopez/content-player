package com.liviolopez.contentplayer.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "updates")

class AppDataStore @Inject constructor(context: Context) {
    private val dataStore = context.dataStore

    private var timeToRefreshDb = 600000L // Milliseconds

    companion object {
        val LAST_DB_UPDATE = longPreferencesKey("LAST_DB_UPDATE")
    }

    private val lastDbUpdate: Flow<Long> = dataStore.data.map {
        it[LAST_DB_UPDATE] ?: 0L
    }

    fun registerDbUpdate(){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit {
                it[LAST_DB_UPDATE] = System.currentTimeMillis()
            }
        }
    }

    suspend fun isDbUpdateNeeded() : Boolean {
        return lastDbUpdate.first() < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(timeToRefreshDb)
    }
}