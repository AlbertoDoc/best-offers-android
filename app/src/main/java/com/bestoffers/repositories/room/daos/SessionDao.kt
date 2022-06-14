package com.bestoffers.repositories.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bestoffers.repositories.room.models.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM Session ORDER BY Session.expires_at ASC LIMIT 1")
    fun get(): Session

    @Insert
    fun insert(session: Session)

    @Delete
    fun delete(session: Session)
}