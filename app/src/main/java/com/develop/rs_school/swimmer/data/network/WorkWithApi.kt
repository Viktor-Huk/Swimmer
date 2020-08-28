package com.develop.rs_school.swimmer.data.network

import android.util.Log
import com.develop.rs_school.swimmer.domain.AgendaStatus
import com.develop.rs_school.swimmer.domain.CustomerLessonWithAgenda
import java.util.*


suspend fun getCustomerLessonsWithFullInfo(customerId: String): MutableList<CustomerLessonWithAgenda> {

    val customer = SwimmerApi.getCustomersImpl().first { it.id == customerId }
    //TODO parallel ?
    val lessonInCalendarList = SwimmerApi.getCustomerCalendarImpl(customerId).sortedBy { it.date }
    val lessonList = SwimmerApi.getCustomerLesson(customerId).sortedByDescending { it.date }

    val resultList = mutableListOf<CustomerLessonWithAgenda>()
    var paidLessonInFuture = customer.paid_lesson
    val notPaidLessonIdsInHistory =
        if (paidLessonInFuture < 0)
            lessonList.map { it.id }.subList(0, -paidLessonInFuture)
        else listOf()
    for (item in lessonInCalendarList) {

        val lessonWithStatus =
            CustomerLessonWithAgenda(
                id = item.id,
                type = item.type,
                date = item.date,
                duration = item.duration
            )
        if (item.status == "1") {
            if (item.date != null && item.date > Date())
                lessonWithStatus.agendaStatus = AgendaStatus.PLANNED
            else
                lessonWithStatus.agendaStatus = AgendaStatus.FORGOT
            //TODO hack
            if (lessonWithStatus.agendaStatus == AgendaStatus.PLANNED && paidLessonInFuture > 0) {
                lessonWithStatus.agendaStatus = AgendaStatus.PREPAID
                paidLessonInFuture--
            }
        }
        if (item.status == "2") {
            lessonWithStatus.agendaStatus = AgendaStatus.CANCELED
        }
        if (item.status == "3") {
            val lessonDetail = lessonList.first { it.id == item.id }
            if (lessonDetail.isAttend == 1) {
                lessonWithStatus.agendaStatus = AgendaStatus.VISIT_PAID
                if(notPaidLessonIdsInHistory.contains(item.id))
                    lessonWithStatus.agendaStatus = AgendaStatus.VISIT_NOT_PAID
            }
            if (lessonDetail.isAttend == 0) {
                lessonWithStatus.agendaStatus = AgendaStatus.MISSED_PAID
                if(notPaidLessonIdsInHistory.contains(item.id))
                    lessonWithStatus.agendaStatus = AgendaStatus.MISSED_NOT_PAID
            }
            if (lessonDetail.isAttend == 0 && lessonDetail.price == "0.00") {
                lessonWithStatus.agendaStatus = AgendaStatus.MISSED_FREE
            }
        }

        resultList.add(lessonWithStatus)
    }
    return resultList
}

suspend fun auth(authData: String) : String {
    SwimmerApi.firstAuth()
    return try {
        val res = SwimmerApi.getCustomersImpl().first { it.phone.contains(authData) }
        Log.d("1", res.name)
        res.id
    } catch (e: NoSuchElementException) {
        Log.d("1", "authError for $authData")
        ""
    }
}