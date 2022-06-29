package com.bestoffers.repositories.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private var retrofit: Retrofit? = null
    private val BASE_URL: String = "http://192.168.0.14:8000/api/v1/"

    fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit as Retrofit
    }
}