package com.bestoffers.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestoffers.repositories.room.entities.Product

class SampleData {
    companion object {
        fun productsSample(): LiveData<List<Product>> {
            val productsList: List<Product> = arrayListOf(
                Product("1", "Carrinho de controle remoto", 70.0, "1", "1"),
                Product("2", "Maquiagem", 100.0, "1", "1")
            )

            return MutableLiveData(productsList)
        }
    }
}