package com.bestoffers.repositories.room.database

import android.content.Context
import androidx.room.Room

class BestOffersDatabase {

    private var databaseInstance: Database? = null

    fun getDatabase(context: Context): Database {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                context.applicationContext, Database::class.java, "best_offers_db"
            ).build()
        }

        return databaseInstance as Database
    }
}