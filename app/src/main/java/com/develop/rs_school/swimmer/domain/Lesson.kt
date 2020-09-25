package com.develop.rs_school.swimmer.domain

import java.util.Date

data class Lesson(
    val id: String,
    val type: String,
    val status: String,
    val date: Date?,
    var agendaStatus: AgendaStatus = AgendaStatus.NONE
)