package com.mdshahsamir.database.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = Date(value)
        return sdf.format(date)
    }

    @TypeConverter
    fun dateToTimestamp(value: String): Long {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return try {
            val date = sdf.parse(value)
            date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}