package com.bestoffers.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bestoffers.repositories.room.daos.ProductDao
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.entities.Product

class HomeViewModel : ViewModel() {

    private val products = MutableLiveData<List<Product>>()

    private var database = BestOffersDatabase()
    private lateinit var productDao: ProductDao

    fun loadDatabase(context: Context) {
        productDao = database.getDatabase(context).productDao()
    }

    fun loadProducts() {
        products.postValue(productDao.getAll())
    }

    fun getProducts(): LiveData<List<Product>> {
        return products
    }
}