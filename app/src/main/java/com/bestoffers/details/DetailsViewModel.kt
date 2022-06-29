package com.bestoffers.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.database.Database
import com.bestoffers.repositories.room.entities.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    lateinit var product: Product
    lateinit var productUid: String

    private lateinit var database: Database

    fun loadDatabase(context: Context) {
        database = BestOffersDatabase().getDatabase(context)
    }

    fun loadProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val productDatabase = database.productDao().getById(productUid)
            if (productDatabase != null) {
                product = productDatabase
            }
        }
    }
}