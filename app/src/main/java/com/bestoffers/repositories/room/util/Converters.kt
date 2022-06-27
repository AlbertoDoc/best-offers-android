package com.bestoffers.repositories.room.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    //List
    @TypeConverter
    fun toArray(value: String?): MutableList<String?>? {
        val listType = object : TypeToken<MutableList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArray(list: MutableList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}
