package com.bestoffers.util

import com.bestoffers.repositories.room.entities.Product

class SampleData {
    companion object {
        fun productsSample(): List<Product> {

            return arrayListOf(
                Product("1", "Carrinho de controle remoto", 70.0, "1", "1"),
                Product("2", "Maquiagem", 100.0, "1", "1")
            )
        }
    }
}