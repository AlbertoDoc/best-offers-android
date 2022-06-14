package com.bestoffers.repositories.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bestoffers.repositories.room.entities.Stablishment

@Dao
interface StablishmentDao {
    @Query("SELECT * FROM Stablishment")
    fun getAll(): List<Stablishment>

    @Insert
    fun insertAll(vararg stablishments: List<Stablishment>)

    @Insert
    fun insert(stablishment: Stablishment)

    @Delete
    fun delete(stablishment: Stablishment)
}