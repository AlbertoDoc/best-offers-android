package com.bestoffers.check_alert

import android.content.Context
import android.util.Log
import com.bestoffers.repositories.retrofit.services.ProductOfInterestService
import com.bestoffers.repositories.room.database.Database
import com.bestoffers.repositories.room.entities.ProductOfInterest
import com.bestoffers.util.NotificationHelper
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.thread

class CheckAlertRunnable(private var database: Database, private val retrofit: Retrofit,
                         val context: Context) : Runnable {

    override fun run() {
        Log.i("CheckAlertRunnable", "Verificando os product of interest")

        val productOfInterestService = retrofit.create(ProductOfInterestService::class.java)
        val session = database.sessionDao().get()

        if (session != null) {
            val request = productOfInterestService.getByUser("Bearer " + session.token)
            request.enqueue(object: Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("CheckAlertRunnable", "Erro ao tentar se comunicar com servidor")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val responseBody = response.body()
                            val productOfInterestJsonArray = responseBody?.get("data")?.asJsonObject
                                ?.get("productsOfInterest")?.asJsonArray

                            if (productOfInterestJsonArray != null) {
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

                                        thread {
                                            if (database.productOfInterestDao().getById(productOfInterest.uid) == null) {
                                                database.productOfInterestDao().insert(productOfInterest)
                                            }

                                            if (productOfInterest.alert) {
                                                val product = database.productDao().getById(productId)

                                                if (product != null) {
                                                    NotificationHelper(product, context).show()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}