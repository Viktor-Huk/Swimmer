package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CustomerCalendar(
    val id: String,
    val type: String, // 1-6 Индивидуальный Групповой Пробный Отработка Индивид.2 Инд+ - от этого разная иконка
    val status: String, // 1 - запланировано, 3 - проведено, 2 - отменено
    @Json(name = "start") val date: Date?,
    val duration: String?
) : Parcelable