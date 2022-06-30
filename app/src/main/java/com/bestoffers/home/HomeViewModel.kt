package com.bestoffers.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestoffers.repositories.retrofit.RetrofitClient
import com.bestoffers.repositories.retrofit.jsonFactories.ProductFactory
import com.bestoffers.repositories.retrofit.services.ProductOfInterestService
import com.bestoffers.repositories.retrofit.services.ProductService
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

class HomeViewModel : ViewModel() {

    var text by mutableStateOf("")

    var products = MutableLiveData<List<Product>>()
    var alerts = MutableLiveData<List<ProductOfInterest>>()
    private val errorMessage = MutableLiveData<String>()
    private val navigateToDetailsPage = MutableLiveData<String>()

    private lateinit var database: Database
    private lateinit var retrofitClient: Retrofit

    fun loadDatabase(context: Context) {
        database = BestOffersDatabase().getDatabase(context)
    }

    fun loadProducts() {
        retrofitClient = RetrofitClient().getRetrofitInstance()

        val productService = retrofitClient.create(ProductService::class.java)
        val request = productService.getProductsByText(ProductFactory().buildGetByTextJson(text))
        
        request.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue("Erro ao se conectar com o servidor.")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        val responseBody = response.body()
                        val jsonArray = responseBody?.get("data")?.asJsonObject?.get("products")?.asJsonArray

                        if (jsonArray != null) {
                            viewModelScope.launch(Dispatchers.IO) {
                                for (jsonObject in jsonArray) {
                                    val productJson = jsonObject.asJsonObject
                                    val product = Product(
                                        productJson.get("id").asString,
                                        productJson.get("name").asString,
                                        productJson.get("price").asDouble,
                                        "",
                                        ""
                                    )

                                    if (database.productDao().getById(product.uid) == null) {
                                        database.productDao().insert(product)
                                    }
                                }

                                products.postValue(database.productDao().getByText("$text%"))
                            }
                        }
                    }
                } else {
                    errorMessage.postValue(response.errorBody()?.string())
                }
            }
        })
    }

    fun loadAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            retrofitClient = RetrofitClient().getRetrofitInstance()

            val productOfInterestService = retrofitClient.create(ProductOfInterestService::class.java)
            val session = database.sessionDao().get()

            if (session != null) {
                val request = productOfInterestService.getByUser("Bearer " + session.token)
                request.enqueue(object: Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        errorMessage.postValue("Erro ao se conectar com o servidor.")
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                val responseBody = response.body()
                                val productOfInterestJsonArray = responseBody?.get("data")?.asJsonObject
                                ?.get("productsOfInterest")?.asJsonArray

                                if (productOfInterestJsonArray != null) {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        for (jsonElement in productOfInterestJsonArray) {
                                            val jsonObject = jsonElement.asJsonObject

                                            val uid = jsonObject?.get("id")?.asString
                                            val startPrice = jsonObject?.get("startPrice")?.asFloat
                                            val endPrice = jsonObject?.get("endPrice")?.asFloat
                                            val alert = jsonObject?.get("alert")?.asBoolean
                                            val userId = jsonObject?.get("userId")?.asString
                                            val productId = jsonObject?.get("productId")?.asString

                                            if (uid != null && startPrice != null && endPrice != null
                                                && alert != null && userId != null && productId != null) {
                                                val productOfInterest = ProductOfInterest(
                                                    uid, startPrice, endPrice, alert, userId, productId
                                                )

                                                if (database.productOfInterestDao().getById(productOfInterest.uid) == null) {
                                                    database.productOfInterestDao().insert(productOfInterest)
                                                }
                                            }
                                        }

                                        alerts.postValue(database.productOfInterestDao().getAll())
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    fun getProducts(): LiveData<List<Product>> {
        return products
    }

    fun getAlerts(): LiveData<List<ProductOfInterest>> {
        return alerts
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun getNavigateToDetailsPage(): LiveData<String> {
        return navigateToDetailsPage
    }

    fun navigateToDetailsPage(productUid: String) {
        navigateToDetailsPage.postValue(productUid)
    }

    fun getProductById(alert: ProductOfInterest): Product? {
        for (product in products.value.orEmpty()) {
            if (product.uid == alert.productUid) {
                return product
            }
        }

        return null
    }
}