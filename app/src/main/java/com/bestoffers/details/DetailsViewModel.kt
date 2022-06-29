package com.bestoffers.details

import androidx.lifecycle.ViewModel
import com.bestoffers.repositories.room.entities.Product

class DetailsViewModel : ViewModel() {

    private lateinit var product: Product

    lateinit var productUid: String
}