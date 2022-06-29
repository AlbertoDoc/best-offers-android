package com.bestoffers.repositories.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bestoffers.repositories.room.entities.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM Product")
    fun getAllLiveData(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg products: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM Product WHERE uid == :uid")
    fun getById(uid: String): Product?

    @Query("SELECT * FROM Product WHERE name LIKE :filter")
    fun getByText(filter: String): List<Product>
}