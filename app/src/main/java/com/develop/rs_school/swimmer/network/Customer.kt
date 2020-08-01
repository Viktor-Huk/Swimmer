package com.develop.rs_school.swimmer.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    val id: String,
    val name: String,
    val dob: String,
    val balance: String,
    @Json(name = "paid_count") val paid_lesson: Int
) : Parcelable


@Parcelize
data class CustomerList(
    val total: Int,
    val items: List<Customer>
) : Parcelable



