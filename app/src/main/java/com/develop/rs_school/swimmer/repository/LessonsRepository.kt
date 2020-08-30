package com.develop.rs_school.swimmer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.develop.rs_school.swimmer.data.database.DatabaseCustomer
import com.develop.rs_school.swimmer.data.database.DatabaseLesson
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.data.database.asDomainModel
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.data.network.asDatabaseModel
import com.develop.rs_school.swimmer.data.network.dto.asDatabaseModel
import com.develop.rs_school.swimmer.data.network.getCustomerLessonsWithFullInfo
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LessonsRepository(private val database: SwimmerDatabase) {

    suspend fun refreshLessons(customerId: String) {
        withContext(Dispatchers.IO) {
            val networkLessons = getCustomerLessonsWithFullInfo(customerId)
            database.lessonDatabaseDao.insertAll(*networkLessons.asDatabaseModel())
        }
    }

    val lessons: LiveData<List<Lesson>> =
        Transformations.map(database.lessonDatabaseDao.getLessons()) { it.asDomainModel()}

    suspend fun refreshCustomer(customerId: String) {
        withContext(Dispatchers.IO) {
            database.customerDatabaseDao.insertCustomer(
                SwimmerApi.getCustomersImpl().first { it.id == customerId }.asDatabaseModel()
            )
        }
    }

    fun getCustomer(customerId: String): LiveData<Customer> =
        Transformations.map(database.customerDatabaseDao.getCustomer(customerId.toInt())) {
            //FIXME after delete cache null problem
            it?.asDomainModel()
            //FIXME after delete cache null problem
                ?: Customer(1, "", "", "", 1, "", "")
        }

    suspend fun clearData(){
        withContext(Dispatchers.IO) {
            database.lessonDatabaseDao.deleteAll()
            database.customerDatabaseDao.deleteAll()
        }
    }

}