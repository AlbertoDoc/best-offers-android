package com.bestoffers.details

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestoffers.repositories.retrofit.RetrofitClient
import com.bestoffers.repositories.retrofit.jsonFactories.ProductOfInterestFactory
import com.bestoffers.repositories.retrofit.services.ProductOfInterestService
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.database.Database
import com.bestoffers.repositories.room.entities.Product
import com.bestoffers.repositories.room.entities.ProductOfInterest
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.roundToInt

class DetailsViewModel : ViewModel() {

    lateinit var product: Product
    lateinit var productUid: String

    private val errorMessage = MutableLiveData<String>()

    var startPrice by mutableStateOf(0.0)
    var endPrice by mutableStateOf(0.0)

    private lateinit var database: Database
    lateinit var retrofitClient: Retrofit

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

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun submitAlert() {
        if (validateForm().isEmpty()) {
            sendAlertCreation()
        } else {
            errorMessage.postValue(validateForm())
        }
    }

    private fun validateForm(): String {
        var errorString = ""
        if (startPrice <= 0.0) {
            errorString += "Insira um preço inicial válido.\n"
        }

        if (endPrice <= 0.0) {
            errorString += "Insira um preço final válido.\n"
        }

        if (startPrice > endPrice) {
            errorString += "O preço final não pode ser menor que o preço inicial.\n"
        }

        return errorString
    }

    fun sendAlertCreation() {
        retrofitClient = RetrofitClient().getRetrofitInstance()

        val productOfInterestService = retrofitClient.create(ProductOfInterestService::class.java)
        val body = ProductOfInterestFactory().buildPostJson(
            product.uid, startPrice.toFloat(), endPrice.toFloat(), true
        )

        val request = productOfInterestService.post(body)
        request.enqueue(object: Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue("Erro ao se conectar com o servidor.")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.code() == 201) {
                        viewModelScope.launch(Dispatchers.IO) {

                        }
                    }
                }
            }
        })
    }
}