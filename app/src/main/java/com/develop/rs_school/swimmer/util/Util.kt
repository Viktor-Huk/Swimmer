package com.develop.rs_school.swimmer.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getPhoneNumber(phone: String): String {
    val filtered = "()+-"
    return phone.filterNot { filtered.indexOf(it) > -1 }
}

fun getDateMinusFormat(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return requireNotNull(sdf.parse(sdf.format(Date())))
}

fun getDateDotFormat(date: Date): Date {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    return requireNotNull(sdf.parse(sdf.format(date)))
}

fun getDateWithOffset(offset: Int, date: Date = Date()): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal.add(Calendar.DATE, offset)
    return cal.time
}

fun getCapitalizingDayOfWeek(date: Date): String {
    val weekPattern = SimpleDateFormat("E")
    return weekPattern.format(date).capitalize()
}

fun getDateDDMM(date: Date): String {
    val datePattern = SimpleDateFormat("dd.MM", Locale("ru"))
    return datePattern.format(date)
}