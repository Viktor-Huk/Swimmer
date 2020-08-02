package com.develop.rs_school.swimmer.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class CustomDateTimeAdapter {
    @ToJson
    fun toJson(value: Date): String {
        return value.toString()
    }

    @FromJson
    fun fromJson(value: String): Date? {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        return parser.parse(value)
    }
}