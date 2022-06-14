package com.bestoffers.repositories.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product (
        @PrimaryKey val uid: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "price") val price: Float,
        @ColumnInfo(name = "stablishment_uid") val stablishmentUid: String?,
        @ColumnInfo(name = "shared_product_uid") val sharedProductUid: String?
)