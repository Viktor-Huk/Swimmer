package com.develop.rs_school.swimmer.data.network

import com.develop.rs_school.swimmer.domain.AgendaStatus
import com.develop.rs_school.swimmer.domain.Lesson
import com.develop.rs_school.swimmer.util.getDateMinusFormat
import java.util.Date

suspend fun SwimmerApi.getCustomerLesson(customerId: String): List<CustomerLesson> {
    val allLessonsContainsDetails = SwimmerApi.getLessonsImpl()

    return allLessonsContainsDetails.filter { lesson ->
        lesson.lessonDetails.find { it.customerId == customerId } != null
    }.map {
        val detail = it.lessonDetails.first { d -> d.customerId == customerId }
        CustomerLesson(
            isAttend = detail.isAttend,
            customerId = detail.customerId,
            reason = detail.reason,
            price = detail.price,
            status = it.status,
            type = it.type,
            date = it.date,
            id = it.id
        )
    }
}

suspend fun SwimmerApi.getCustomerLessonsWithFullInfo(customerId: Int):
        MutableList<CustomerLessonWithAgenda> {

    val customer = SwimmerApi.getCustomersImpl().first { it.id == customerId }

    val lessonInCalendarList =
        SwimmerApi.getCustomerCalendarImpl(customerId.toString()).sortedBy { it.date }
    val lessonList = getCustomerLesson(customerId.toString()).sortedByDescending { it.date }

    val resultList = mutableListOf<CustomerLessonWithAgenda>()
    var paidLessonInFuture = customer.paidLesson
    val notPaidLessonIdsInHistory =
        if (paidLessonInFuture < 0)
            lessonList.map { it.id }.subList(0, -paidLessonInFuture)
        else listOf()
    for (item in lessonInCalendarList) {

        val lessonWithStatus =
            CustomerLessonWithAgenda(
                id = item.id,
                type = item.type,
                status = item.status,
                date = item.date
            )
        if (item.status == "1") {
            if (item.date != null && item.date > getDateMinusFormat())
                lessonWithStatus.agendaStatus = AgendaStatus.PLANNED
            else
                lessonWithStatus.agendaStatus = AgendaStatus.FORGOT
            // TODO hack
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
                if (notPaidLessonIdsInHistory.contains(item.id))
                    lessonWithStatus.agendaStatus = AgendaStatus.VISIT_NOT_PAID
            }
            if (lessonDetail.isAttend == 0) {
                lessonWithStatus.agendaStatus = AgendaStatus.MISSED_PAID
                if (notPaidLessonIdsInHistory.contains(item.id))
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

// TODO move to other file
// FIXME !!!!!!!!!!!!!!!!!!!!!!!!!!!
data class CustomerLesson(
    val id: String,
    val type: String,
    val status: String,
    val date: Date?,
    val customerId: String,
    val reason: String?, // причина пропуска - от этого зависит стиль айтема в табличке с занятиями
    val isAttend: Int, // был или пропустил занятие
    val price: String
)

data class CustomerLessonWithAgenda(
    val id: String,
    val type: String, // 1-6 Индивидуальный Групп Пробный Отработка Индивид.2 Инд+
    val status: String, // 1 - запланировано, 3 - проведено, 2 - отменено
    val date: Date?,
    var agendaStatus: AgendaStatus = AgendaStatus.NONE
)

// FIXME it is == getCustomerLessonsWithFullInfo ? above
fun MutableList<CustomerLessonWithAgenda>.asDomainModel(): List<Lesson> {
    return this.map {
        Lesson(
            id = it.id,
            type = it.type,
            status = it.status,
            date = it.date,
            agendaStatus = it.agendaStatus
        )
    }
}