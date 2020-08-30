package com.develop.rs_school.swimmer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
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

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toAgenda(value: Int) = enumValues<AgendaStatus>()[value]

    @TypeConverter
    fun fromAgenda(value: AgendaStatus) = value.ordinal
}

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