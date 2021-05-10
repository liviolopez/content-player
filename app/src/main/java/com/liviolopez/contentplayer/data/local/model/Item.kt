package com.liviolopez.contentplayer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val format: String,

    val drmUuid: String?,
    val drmLicense: String?,
)