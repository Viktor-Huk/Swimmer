package com.develop.rs_school.swimmer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.develop.rs_school.swimmer.AgendaStatus
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

    companion object {
        @Volatile
        private var INSTANCE: SwimmerDatabase? = null

        fun getInstance(context: Context): SwimmerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SwimmerDatabase::class.java,
                        "swimmer_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
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
