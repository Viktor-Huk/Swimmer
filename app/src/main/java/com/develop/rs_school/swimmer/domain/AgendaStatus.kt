package com.develop.rs_school.swimmer.domain

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
    PAUSE // don't use
}