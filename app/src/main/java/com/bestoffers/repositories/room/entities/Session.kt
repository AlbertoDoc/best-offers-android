package com.bestoffers.repositories.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Session (
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "user_uid") val userUid: String,
    @ColumnInfo(name = "token") val token: String,
    @ColumnInfo(name = "active") val active: Boolean
)