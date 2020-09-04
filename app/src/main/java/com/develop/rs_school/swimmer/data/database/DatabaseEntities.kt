package com.develop.rs_school.swimmer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.develop.rs_school.swimmer.AgendaStatus
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import java.util.*

@Entity(tableName = "customer")
data class DatabaseCustomer(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dob: String, //TODO date type - no, in bad API
    val balance: String, // сколько осталось денег
    val paidLesson: Int, // сколько осталось посещений при таком балансе и занятиях
    val phone: String,
    val email: String
)

@Entity(tableName = "lesson")
data class DatabaseLesson(
    @PrimaryKey
    val id: String,
    val type: String,
    val status: String,
    val date: Date?,
    val agendaStatus: AgendaStatus
)

fun DatabaseCustomer.asDomainModel(): Customer{
    return Customer(
        id = this.id,
        name = this.name,
        dob = this.dob,
        balance = this.balance,
        paid_lesson = this.paidLesson,
        phone = this.phone,
        email = this.email
    )
}

fun Customer.asDatabaseModel(): DatabaseCustomer{
    return DatabaseCustomer(
        id = this.id,
        name = this.name,
        dob = this.dob,
        balance = this.balance,
        paidLesson = this.paid_lesson,
        phone = this.phone,
        email = this.email
    )
}

fun List<DatabaseLesson>.asDomainModel(): List<Lesson>{
    return map {
        Lesson(
            id = it.id,
            type = it.type,
            status = it.status,
            date = it.date,
            agendaStatus = it.agendaStatus
        )
    }
}

fun List<Lesson>.asDatabaseModel(): Array<DatabaseLesson>{
    return map {
        DatabaseLesson(
            id = it.id,
            type = it.type,
            status = it.status,
            date = it.date,
            agendaStatus = it.agendaStatus
        )
    }.toTypedArray()
}