package com.bestoffers.repositories.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bestoffers.repositories.room.entities.SharedProduct

@Dao
interface SharedProductDao {
    @Query("SELECT * FROM SharedProduct")
    fun getAll(): List<SharedProduct>

    @Insert
    fun insertAll(sharedProducts: List<SharedProduct>)

    @Insert
    fun insert(sharedProduct: SharedProduct)

    @Delete
    fun delete(sharedProduct: SharedProduct)
}