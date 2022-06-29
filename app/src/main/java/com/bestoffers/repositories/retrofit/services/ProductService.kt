package com.bestoffers.repositories.retrofit.services

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface ProductService {

    @POST("products/")
    fun getProductsByText(@Body jsonObject: JsonObject): Call<JsonObject>
}