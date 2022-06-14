package com.bestoffers.repositories.retrofit.services

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ProductOfInterestService {

    @GET("/")
    fun getByUserId(userId: String): Call<JsonObject>

    @POST("/")
    fun post(@Body json: JsonObject): Call<JsonObject>

    @PUT("/{uid}/alertdown")
    fun put(@Query("uid") uid: String): Call<JsonObject>
}