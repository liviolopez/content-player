package com.liviolopez.contentplayer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey val id: String,
    val name: String,
    val format: String,
    val url: String,
    val thumbnail: String
)