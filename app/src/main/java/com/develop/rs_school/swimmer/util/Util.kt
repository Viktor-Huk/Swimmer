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
    val sdf = simpleDateFormatWrapper("yyyy-MM-dd")
    return requireNotNull(sdf.parse(sdf.format(Date())))
}

fun getDateDotFormat(date: Date): Date {
    val sdf = simpleDateFormatWrapper("dd.MM.yyyy")
    return requireNotNull(sdf.parse(sdf.format(date)))
}

fun parseDateDotFormatFromString(dateString: String): Date {
    val parser = simpleDateFormatWrapper("dd.MM.yyyy")
    return parser.parse(dateString) ?: Date()
}

fun getDateWithOffset(offset: Int, date: Date = Date()): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal.add(Calendar.DATE, offset)
    return cal.time
}

fun getCapitalizingDayOfWeek(date: Date): String {
    val weekPattern = simpleDateFormatWrapper("E")
    return weekPattern.format(date).capitalize()
}

fun getDateDDMM(date: Date): String {
    val datePattern = simpleDateFormatWrapper("dd.MM")
    return datePattern.format(date)
}

fun simpleDateFormatWrapper(format: String): SimpleDateFormat {
    return SimpleDateFormat(format, Locale("ru"))
}