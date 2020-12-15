package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthObject(
    @Json(name = "phoneNumber") var phone: String
) : Parcelable

@Parcelize
data class TokenObject(
    @Json(name = "id") val id: Int,
    @Json(name = "type") val type : String,
    @Json(name = "accessToken") val accessToken : String,
    @Json(name = "refreshToken") val refreshToken : String,
    @Json(name = "expiresIn") val expiresIn : Int) : Parcelable

@Parcelize
data class LessonFilterObject(
    val status: Int,
    val page: Int,
    @Json(name = "id") val ids: List<String> = listOf()
) : Parcelable

@Parcelize
data class CustomerFilterObject(
    val page: Int,
    @Json(name = "id") val customerId: Int = 0
) : Parcelable

@Parcelize
data class TariffFilterObject(
    val page: Int
) : Parcelable