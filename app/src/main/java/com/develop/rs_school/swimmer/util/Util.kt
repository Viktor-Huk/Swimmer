package com.develop.rs_school.swimmer.util

import java.text.SimpleDateFormat
import java.util.*

enum class AgendaStatus {
    NONE,
    PLANNED,
    PREPAID,
    VISIT_PAID,
    VISIT_NOT_PAID,
    MISSED_NOT_PAID,
    MISSED_FREE,
    MISSED_PAID,
    FORGOT,
    CANCELED,
    PAUSE //don't use
}

fun getPhoneNumber(phone: String): String {
    val filtered = "()+-"
    return phone.filterNot { filtered.indexOf(it) > -1 }
}

fun getDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return requireNotNull(sdf.parse(sdf.format(Date())))
}