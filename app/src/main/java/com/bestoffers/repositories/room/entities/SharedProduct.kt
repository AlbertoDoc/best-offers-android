package com.bestoffers.repositories.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SharedProduct")
data class SharedProduct (
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "stablishment_name") val stablishmentName: String,
    @ColumnInfo(name = "product_uid") val productUid: String,
    @ColumnInfo(name = "user_uid") val userUid: String
)