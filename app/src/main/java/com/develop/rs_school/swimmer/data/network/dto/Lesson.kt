package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class LessonList(
    val total: Int,
    @Json(name = "count") val countInPage: Int,
    val page: Int,
    val items: List<Lesson>
) : Parcelable

@Parcelize
data class Lesson(
    val id: String,
    @Json(name = "time_from") val date: Date?,
    @Json(name = "time_to") val dateEnd: String,
    @Json(name = "lesson_type_id") val type: String,
    val status: String,
    @Json(name = "details") val lessonDetails: List<LessonDetailsByCustomer>
) : Parcelable

@Parcelize
data class LessonDetailsByCustomer(
    val id: String,
    @Json(name = "customer_id") val customerId: String,
    @Json(name = "reason_id") val reason: String?,
    @Json(name = "is_attend") val isAttend: Int,
    @Json(name = "commission") val price: String
) : Parcelable