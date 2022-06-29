package com.bestoffers.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val navigationMessage = MutableLiveData<String>()

    private lateinit var database: Database

    fun loadDatabase(context: Context) {
        database = BestOffersDatabase().getDatabase(context)
    }

    fun getNavigationMessage(): LiveData<String> {
        return navigationMessage
    }

    fun automaticLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            if (database.userDao().getAll().isNotEmpty()) {
                navigationMessage.postValue("Ok")
            }
        }
    }
}