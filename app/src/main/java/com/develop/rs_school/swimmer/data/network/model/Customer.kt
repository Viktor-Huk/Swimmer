package com.develop.rs_school.swimmer.data.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Customer(
    val id: String,
    val name: String,
    val dob: String,
    val balance: String, // сколько осталось денег
    @Json(name = "paid_count") val paid_lesson: Int, // сколько осталось посещений при таком балансе и занятиях
    @Json(name = "paid_till") val paidDate: Date?,
    val phone: List<String>,
    val email: List<String>
) : Parcelable


@Parcelize
data class CustomerList(
    val total: Int,
    val items: List<Customer>
) : Parcelable



