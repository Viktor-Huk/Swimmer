package com.develop.rs_school.swimmer.domain

import com.develop.rs_school.swimmer.AgendaStatus
import java.util.*

data class Lesson(
    val id: String,
    val type: String,
    val status: String,
    val date: Date?,
    var agendaStatus: AgendaStatus = AgendaStatus.NONE
)
