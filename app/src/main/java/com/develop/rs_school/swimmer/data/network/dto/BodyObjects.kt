package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhoneAuthObject(
    @Json(name = "phoneNumber") var phone: String
) : Parcelable

@Parcelize
data class Token2Object(
    @Json(name = "id") val id: Int,
    @Json(name = "type") val type : String,
    @Json(name = "accessToken") val accessToken : String,
    @Json(name = "refreshToken") val refreshToken : String,
    @Json(name = "expiresIn") val expiresIn : Int) : Parcelable

@Parcelize
data class TokenObject(val token: String) : Parcelable

@Parcelize
data class LessonStatusObject(val status: String) : Parcelable