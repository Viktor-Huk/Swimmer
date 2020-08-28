package com.develop.rs_school.swimmer.data.network.model

import com.squareup.moshi.Json
import java.util.*

data class CustomerLesson(
    val id: String,
    val type: String,
    val status: String,
    @Json(name = "time_from") val date: Date?,
    val customerId: String,
    val reason: String?, // причина пропуска - от этого зависит стиль айтема в табличке с занятиями
    val isAttend: Int, // был или пропустил занятие
    val price: String
)