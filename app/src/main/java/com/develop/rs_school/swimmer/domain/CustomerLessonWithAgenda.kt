package com.develop.rs_school.swimmer.domain

import java.util.*

enum class AgendaStatus(val i: Int) {
    NONE(0),
    PLANNED(1),
    PREPAID(2),
    VISIT_PAID(3),
    VISIT_NOT_PAID(4),
    MISSED_NOT_PAID(5),
    MISSED_FREE(6),
    MISSED_PAID(7),
    FORGOT(8),
    CANCELED(9)
    //, PAUSE(10) don't use
}

data class CustomerLessonWithAgenda(
    val id: String,
    val type: String, // 1-6 Индивидуальный Групповой Пробный Отработка Индивид.2 Инд+ - от этого разная иконка
//    val status: String, // 1 - запланировано, 3 - проведено, 2 - отменено
    val date: Date?,
    val duration: String?,
    //val customerId: String,
    //val reason: String? = null, // причина пропуска - от этого зависит стиль айтема в табличке с занятиями
    //val isAttend: Int? = null, // был или пропустил занятие
    //val price: String? = null,
    var agendaStatus: AgendaStatus = AgendaStatus.NONE
)
