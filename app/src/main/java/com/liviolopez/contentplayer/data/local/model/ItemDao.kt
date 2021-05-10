package com.liviolopez.contentplayer.data.local.model

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)

    @Query("DELETE FROM item")
    suspend fun deleteAllItems()

    @Query("SELECT COUNT(*) FROM item")
    suspend fun getCountItems(): Int

    @RawQuery(observedEntities = [Item::class])
    fun getItemRaw(query: SupportSQLiteQuery): Flow<List<Item>>

    /**
     * Query maker for Item table to then run it with @RawQuery annotation
     */
    fun createSimpleQuery(formats: List<String>, protection: String) : SimpleSQLiteQuery {
        var query = "SELECT * FROM item"
        val conditions = mutableListOf<String>()
        val params = mutableListOf<Any>()

        if(formats.isNotEmpty()) {
            val _formats = formats.joinToString(",") { "'${it}'" }
            conditions.add("LOWER(format) IN ($_formats)")
        }

        if(protection.lowercase() == "without"){
            conditions.add("(drmUuid IS NULL OR drmLicense IS NULL)")
        } else if(protection != "Anyone") {
            conditions.add("LOWER(drmUuid) = ?")
            params.add(protection.lowercase())
        }

        if(conditions.isNotEmpty()){
            query += " WHERE " + conditions.joinToString(" AND ")
        }

        return SimpleSQLiteQuery(query, params.toTypedArray())
    }
}