package com.bestoffers.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestoffers.repositories.retrofit.RetrofitClient
import com.bestoffers.repositories.retrofit.jsonFactories.AuthFactory
import com.bestoffers.repositories.retrofit.services.AuthService
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.database.Database
import com.bestoffers.repositories.room.entities.Session
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class MainActivityViewModel : ViewModel() {

    private val navigationMessage = MutableLiveData<String>()

    private lateinit var retrofitClient: Retrofit
    private lateinit var database: Database

    fun loadDatabase(context: Context) {
        database = BestOffersDatabase().getDatabase(context)
    }

    fun getNavigationMessage(): LiveData<String> {
        return navigationMessage
    }

    fun automaticLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = database.userDao().getAll()
            if (users.isNotEmpty()) {
                val user = users[0]
                retrofitClient = RetrofitClient().getRetrofitInstance()

                val authService = retrofitClient.create(AuthService::class.java)
                val body = AuthFactory.buildLoginJson(user.email, user.password)

                val request = authService.postLogin(body)
                request.enqueue(object: Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                val token = response.body()?.get("data")?.asJsonObject?.get("token")?.asString
                                val session =
                                    token?.let {
                                        Session(
                                            UUID.randomUUID().toString(),
                                            user.uid,
                                            it,
                                            true
                                        )
                                    }

                                if (session != null) {
                                    //TODO more than 1 session is storaged
                                    viewModelScope.launch(Dispatchers.IO) {
                                        val sessionDatabase = database.sessionDao().get()
                                        if (sessionDatabase != null) {
                                            database.sessionDao().delete(session)
                                        }
                                        database.sessionDao().insert(session)
                                        navigationMessage.postValue("Ok")
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }
}