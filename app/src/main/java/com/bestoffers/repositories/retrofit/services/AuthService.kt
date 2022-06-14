package com.bestoffers.repositories.retrofit.services

import com.google.gson.JsonObject
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body

interface AuthService {

    @POST("/signup")
    fun postSignup(@Body json: JsonObject): Call<JsonObject>

    @POST("/login")
    fun postLogin(@Body json: JsonObject): Call<JsonObject>
}