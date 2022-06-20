package com.bestoffers.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
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

    private val errorMessage = MutableLiveData<String>()

    lateinit var retrofitClient: Retrofit

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun validateForm(): Boolean {
        var errorString = "";
        if (firstName.isEmpty()) {
            errorString += "Preencha o primeiro nome\n"
        }

        if (lastName.isEmpty()) {
            errorString += "Preencha o sobrenome\n"
        }

        if (email.isEmpty()) {
            errorString += "Preencha o email\n"
        }

        if (password.isEmpty()) {
            errorString += "Preencha a senha\n"
        }

        if (confirmPassword.isEmpty()) {
            errorString += "Preencha a confirmação de senha"
        }

        if (errorString.isNotEmpty()) {
            errorMessage.postValue(errorString)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorString = "Email inválido."
            errorMessage.postValue(errorString)
            return false
        }

        if (password != confirmPassword) {
            errorString = "As senhas não são iguais."
            errorMessage.postValue(errorString)
            return false
        }

        return true
    }

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