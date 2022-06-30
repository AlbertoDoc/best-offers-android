package com.bestoffers.repositories.room.daos

import androidx.room.*
import com.bestoffers.repositories.room.entities.ProductOfInterest

@Dao
interface ProductOfInterestDao {
    @Query("SELECT * FROM ProductOfInterest")
    fun getAll(): List<ProductOfInterest>

    @Insert
    fun insertAll(productOfInterest: List<ProductOfInterest>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productOfInterest: ProductOfInterest)

    @Delete
    fun delete(productOfInterest: ProductOfInterest)

    @Query("SELECT * FROM ProductOfInterest WHERE uid == :uid")
    fun getById(uid: String): ProductOfInterest?
}