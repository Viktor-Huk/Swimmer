package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TariffList(
    val total: Int,
    @Json(name = "count") val countInPage: Int,
    val page: Int,
    val items: List<Tariff>
) : Parcelable

@Parcelize
data class Tariff(
    val id: String,
    @Json(name = "customer_id") val customerId: Int,
    @Json(name = "subject_ids") val subjects: List<Int>,
    @Json(name = "lesson_type_ids") val lessonTypes: List<Int>,
    @Json(name = "b_date") val dateStart: String,
    @Json(name = "e_date") val dateEnd: String
) : Parcelable