package com.bestoffers.register

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestoffers.repositories.retrofit.RetrofitClient
import com.bestoffers.repositories.retrofit.jsonFactories.AuthFactory
import com.bestoffers.repositories.retrofit.services.AuthService
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.database.Database
import com.bestoffers.repositories.room.entities.User
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val navigationMessage = MutableLiveData<String>()

    lateinit var retrofitClient: Retrofit
    lateinit var database: Database

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun getNavigationMessage(): LiveData<String> {
        return navigationMessage
    }

    fun loadDatabase(context: Context) {
        database = BestOffersDatabase().getDatabase(context)
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

        if (password.length < 10) {
            errorString += "A senha precisa ter mais de 10 caracteres\n"
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
                    if (response.code() == 201) {
                        val responseBody = response.body()
                        val user = responseBody?.get("data")?.asJsonObject?.get("id")
                            ?.let { User(
                                it.asString, firstName, lastName, email,
                                password, true, mutableListOf(), mutableListOf())
                            }

                        if (user != null) {
                            viewModelScope.launch(Dispatchers.IO) {
                                database.userDao().insert(user)
                            }
                            navigationMessage.postValue("HomeActivity")
                        } else {
                            errorMessage.postValue("Não foi possível salvar o usuário")
                        }
                    }
                } else {
                    errorMessage.postValue(response.errorBody()?.string())
                }
            }
        })
    }
}