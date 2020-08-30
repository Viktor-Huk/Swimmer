package com.develop.rs_school.swimmer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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