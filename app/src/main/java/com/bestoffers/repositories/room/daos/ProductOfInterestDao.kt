package com.bestoffers.repositories.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bestoffers.repositories.room.models.ProductOfInterest

@Dao
interface ProductOfInterestDao {
    @Query("SELECT * FROM ProductOfInterest")
    fun getAll(): List<ProductOfInterest>

    @Insert
    fun insertAll(vararg productOfInterest: List<ProductOfInterest>)

    @Insert
    fun insert(productOfInterest: ProductOfInterest)

    @Delete
    fun delete(productOfInterest: ProductOfInterest)
}