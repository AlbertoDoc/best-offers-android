package com.bestoffers.repositories.retrofit.services

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ProductOfInterestService {

    @GET("productsOfInterest/")
    fun getByUser(@Header("Authorization") token: String): Call<JsonObject>

    @POST("productsOfInterest/")
    fun post(@Body json: JsonObject, @Header("Authorization") token: String): Call<JsonObject>

    @PUT("productsOfInterest/{uid}/alertdown")
    fun put(@Query("uid") uid: String): Call<JsonObject>
}