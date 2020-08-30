package com.develop.rs_school.swimmer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.develop.rs_school.swimmer.data.database.DatabaseCustomer
import com.develop.rs_school.swimmer.data.database.DatabaseLesson
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.data.network.getCustomerLessonsWithFullInfo
import com.develop.rs_school.swimmer.domain.AgendaStatus
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.CustomerLessonWithAgenda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LessonsRepository(private val database: SwimmerDatabase) {

    suspend fun refreshLessons(customerId : String) {
        withContext(Dispatchers.IO) {
            val lessons = getCustomerLessonsWithFullInfo(customerId)
            val t = lessons.map {
                DatabaseLesson(
                    id = it.id,
                    type = it.type,
                    date = it.date,
                    agendaStatusValue = it.agendaStatus.i)
            }.toTypedArray()
            database.lessonDatabaseDao.insertAll(*t)
        }
    }

    val lessons: LiveData<List<CustomerLessonWithAgenda>> =
        Transformations.map(database.lessonDatabaseDao.getLessons()) {
            val t = mutableListOf<CustomerLessonWithAgenda>()
            for(i in it) {
                t.add(
                CustomerLessonWithAgenda(
                    id = i.id,
                    type = i.type,
                    date = i.date,
                    agendaStatus = AgendaStatus.VISIT_PAID //FIXME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                ))
            }
            t
        }

    suspend fun refreshCustomer(customerId : String) {
        withContext(Dispatchers.IO) {
            val c = SwimmerApi.getCustomersImpl().first { it.id == customerId}

            database.customerDatabaseDao.insertCustomer(
                DatabaseCustomer(
                    c.id.toInt(), c.name, c.dob, c.balance, c.paid_lesson, c.phone.firstOrNull() ?: "", c.email.firstOrNull() ?: ""
                )
            )
        }
    }

    fun getCustomer(customerId : String): LiveData<Customer> =
        Transformations.map(database.customerDatabaseDao.getCustomer(customerId.toInt())){
            Customer(
                id = it.id,
                name = it.name,
                dob = it.dob,
                balance = it.balance,
                paid_lesson = it.paid_lesson,
                phone = it.phone,
                email = it.email
            )
        }

}