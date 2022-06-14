package com.bestoffers.repositories.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [ Index(value = ["email"], unique = true) ])
data class User (
    @PrimaryKey val uid : String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "active") val active: Boolean,
    @ColumnInfo(name = "shared_products_uid") val sharedProductsUid: List<String>?,
    @ColumnInfo(name = "products_of_interest_uid") val productsOfInterestUid: List<String>?
)