package com.bestoffers.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bestoffers.repositories.retrofit.RetrofitClient
import com.bestoffers.repositories.retrofit.jsonFactories.AuthFactory
import com.bestoffers.repositories.retrofit.services.AuthService
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RegisterViewModel : ViewModel() {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    val errorMessage = MutableLiveData<String>()

    lateinit var retrofitClient: Retrofit

    fun sendRegistration() {
        retrofitClient = RetrofitClient().getRetrofitInstance()

        val authService = retrofitClient.create(AuthService::class.java)
        val body = AuthFactory.buildSignupJson(firstName, lastName, email, password, confirmPassword)

        val request = authService.postSignup(body)
        request.enqueue(object: Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue("Erro ao se conectar com o servidor.")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        // TODO: Do navigation to main screen
                    }
                } else {
                    errorMessage.postValue("Erro ao se conectar com o servidor.")
                }
            }
        })
    }
}