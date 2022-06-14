package com.bestoffers.repositories.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductOfInterest")
data class ProductOfInterest (
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "start_price") val startPrice: Float,
    @ColumnInfo(name = "end_price") val endPrice: Float,
    @ColumnInfo(name = "alert") val alert: Boolean,
    @ColumnInfo(name = "product_uid") val productUid: String,
    @ColumnInfo(name = "user_uid") val userUid: String
)