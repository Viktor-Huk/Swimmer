package com.develop.rs_school.swimmer.model

enum class PayStatus(val i: Int) {
    PAYED(2),
    NOTPAYED(1),
    NONE(0)
}

//TODO Оставить только значиние легенды для ресайклера!!!!!!!!!!!!!!!!!!!!!!!!
data class CustomerLessonWithFullInfo(
    val id: String,
    val type: String, // 1-6 Индивидуальный Групповой Пробный Отработка Индивид.2 Инд+ - от этого разная иконка
    val status: String, // 1 - запланировано, 3 - проведено, 2 - отменено
    val date: String,
    val duration: String,
    val customerId: String,
    val reason: String?, // причина пропуска - от этого зависит стиль айтема в табличке с занятиями
    val isAttend: String?, // был или пропустил занятие
    val price: String? = null,
    val payStatus: PayStatus = PayStatus.NONE
)
