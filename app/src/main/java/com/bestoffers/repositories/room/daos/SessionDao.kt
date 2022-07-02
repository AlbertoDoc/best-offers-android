package com.bestoffers.repositories.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bestoffers.repositories.room.entities.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM Session LIMIT 1")
    fun get(): Session?

    @Query("SELECT * FROM Session")
    fun getAll(): List<Session>

    @Insert
    fun insert(session: Session)

    @Delete
    fun delete(session: Session)

    @Delete
    fun deleteAll(sessions: List<Session>)
}