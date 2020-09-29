package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.develop.rs_school.swimmer.data.database.DatabaseCustomer
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class CustomerList(
    val total: Int,
    val items: List<Customer>
) : Parcelable

@Parcelize
data class Customer(
    val id: Int,
    val name: String,
    val dob: String,
    val balance: String,
    @Json(name = "paid_count") val paidLesson: Int, // ост. посещений при таком балансе и занятиях
    @Json(name = "paid_till") val paidDate: Date?,
    val phone: List<String>,
    val email: List<String>
) : Parcelable

fun Customer.asDatabaseModel(): DatabaseCustomer {
    return DatabaseCustomer(
        id = this.id,
        name = this.name,
        dob = this.dob,
        balance = this.balance,
        paidLesson = this.paidLesson,
        phone = this.phone.firstOrNull() ?: "",
        email = this.email.firstOrNull() ?: ""
    )
}

fun Customer.asDomainModel(): com.develop.rs_school.swimmer.domain.Customer {
    return com.develop.rs_school.swimmer.domain.Customer(
        id = this.id,
        name = this.name,
        dob = this.dob,
        balance = this.balance,
        paidLesson = this.paidLesson,
        phone = this.phone.firstOrNull() ?: "",
        email = this.email.firstOrNull() ?: ""
    )
}