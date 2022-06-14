package com.bestoffers.repositories.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.bestoffers.repositories.room.daos.*
import com.bestoffers.repositories.room.entities.*

@Database(entities = [Product::class, ProductOfInterest::class, Session::class,
    SharedProduct::class, Stablishment::class, User::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun productOfInterestDao(): ProductOfInterestDao
    abstract fun sessionDao(): SessionDao
    abstract fun sharedProductDao(): SharedProductDao
    abstract fun stablishmentDao(): StablishmentDao
    abstract fun userDao(): UserDao
}