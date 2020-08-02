package com.develop.rs_school.swimmer.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerCalendarItem(
    val id: String,
    val type: String, // 1-6 Индивидуальный Групповой Пробный Отработка Индивид.2 Инд+ - от этого разная иконка
    val status: String, // 1 - запланировано, 3 - проведено, 2 - отменено
    @Json(name = "start") val date: String?,
    val duration: String
) : Parcelable