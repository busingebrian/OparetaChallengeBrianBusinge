package com.leroy.oparetachallenge.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leroy.oparetachallenge.models.ConversionQuote
import java.util.*

object RoomTypeConverters {

    @TypeConverter
    fun dateFromLong(time: Long?): Date {
        return Date(time ?: 0)
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long {
        return date?.time ?: 0
    }

    @JvmStatic
    @TypeConverter
    fun fromString(value: String): Map<String, ConversionQuote> {
        val mapType = object : TypeToken<Map<String, ConversionQuote>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringMap(map: Map<String, ConversionQuote>): String {
        val gson = Gson()
        return gson.toJson(map)
    }

}