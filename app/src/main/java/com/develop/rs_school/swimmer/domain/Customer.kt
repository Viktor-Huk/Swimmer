package com.develop.rs_school.swimmer.domain

data class Customer(
    val id: Int,
    val name: String,
    val dob: String, // TODO date type
    val balance: String,
    val paidLesson: Int, // сколько осталось посещений при таком балансе и занятиях
    val phone: String,
    val email: String
)