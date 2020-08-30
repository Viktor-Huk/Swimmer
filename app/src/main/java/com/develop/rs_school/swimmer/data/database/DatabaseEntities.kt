package com.develop.rs_school.swimmer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.develop.rs_school.swimmer.domain.AgendaStatus
import java.util.*

@Entity(tableName = "customer")
data class DatabaseCustomer(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dob: String, //TODO date type
    val balance: String, // сколько осталось денег
    val paid_lesson: Int, // сколько осталось посещений при таком балансе и занятиях
    val phone: String,
    val email: String
)

@Entity(tableName = "lesson")
data class DatabaseLesson(
    @PrimaryKey
    val id: String,
    val type: String,
    val date: Date?,
    var agendaStatusValue: Int = 0 //TODO add TypeConverter for enum type
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
