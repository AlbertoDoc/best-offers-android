package com.bestoffers.repositories.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bestoffers.repositories.room.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User")
    fun getAllLiveData(): LiveData<List<User>>

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}