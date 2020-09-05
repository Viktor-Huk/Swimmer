package com.develop.rs_school.swimmer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.develop.rs_school.swimmer.util.AgendaStatus
import java.util.*

@Database(
    entities = [DatabaseCustomer::class, DatabaseLesson::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SwimmerDatabase : RoomDatabase() {
    abstract val customerDatabaseDao: CustomerDao
    abstract val lessonDatabaseDao: LessonDao
}

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
