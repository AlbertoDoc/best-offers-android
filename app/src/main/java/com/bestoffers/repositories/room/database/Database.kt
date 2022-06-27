package com.bestoffers.repositories.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bestoffers.repositories.room.daos.*
import com.bestoffers.repositories.room.entities.*
import com.bestoffers.repositories.room.util.Converters

@Database(entities = [Product::class, ProductOfInterest::class, Session::class,
    SharedProduct::class, Stablishment::class, User::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun productOfInterestDao(): ProductOfInterestDao
    abstract fun sessionDao(): SessionDao
    abstract fun sharedProductDao(): SharedProductDao
    abstract fun stablishmentDao(): StablishmentDao
    abstract fun userDao(): UserDao
}