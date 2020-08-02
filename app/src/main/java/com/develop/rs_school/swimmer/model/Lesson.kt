package com.develop.rs_school.swimmer.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lesson(
    val id: String,
    val date: String,
    @Json(name = "time_from") val timeFrom: String,
    @Json(name = "time_to") val timeTo: String,
    @Json(name = "lesson_type_id") val type: String,
    @Json(name = "details") val lessonDetails: List<LessonDetailsByCustomer>
) : Parcelable

@Parcelize
data class LessonDetailsByCustomer(
    val id: String,
    @Json(name = "customer_id") val customerId: String,
    @Json(name = "reason_id") val reason: String?,
    @Json(name = "is_attend") val isAttend: String,
    @Json(name = "commission") val price: String
) : Parcelable


@Parcelize
data class LessonList(
    val total: Int,
    val items: List<Lesson>
) : Parcelable