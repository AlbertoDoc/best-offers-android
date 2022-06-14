package com.bestoffers.repositories.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bestoffers.repositories.room.entities.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getAll(): List<Product>

    @Insert
    fun insert(product: Product)

    @Insert
    fun insertAll(vararg products: Product)

    @Delete
    fun delete(product: Product)
}