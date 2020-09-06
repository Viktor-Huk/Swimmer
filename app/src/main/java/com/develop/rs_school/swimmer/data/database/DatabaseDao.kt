package com.develop.rs_school.swimmer.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomerDao {
    @Query("select * from customer where id = :customerId")
    fun getCustomer(customerId: Int): LiveData<DatabaseCustomer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: DatabaseCustomer)

    @Query("DELETE FROM customer")
    suspend fun deleteAll()
}

@Dao
interface LessonDao {
    @Query("select * from lesson")
    fun getLessons(): LiveData<List<DatabaseLesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg lessons: DatabaseLesson)

    @Query("DELETE FROM lesson")
    suspend fun deleteAll()
}