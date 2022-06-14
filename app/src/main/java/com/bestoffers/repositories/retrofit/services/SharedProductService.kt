package com.bestoffers.repositories.retrofit.services

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface SharedProductService {

    @GET("/")
    fun get(userId: String): Call<JsonObject>

    @POST("/")
    fun post(@Body json: JsonObject): Call<JsonObject>

    @DELETE
    fun delete(userId: String, sharedProductId: String)
}