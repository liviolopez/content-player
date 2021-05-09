package com.liviolopez.contentplayer.data.local.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE item.id LIKE '%' || :query || '%'")
    fun searchItem(query: String): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)

    @Query("DELETE FROM item")
    suspend fun deleteAllItems()

    @Query("SELECT COUNT(*) FROM item")
    suspend fun getCountItems(): Int
}