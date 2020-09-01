package com.develop.rs_school.swimmer

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
