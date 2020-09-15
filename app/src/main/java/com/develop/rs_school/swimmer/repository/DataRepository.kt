package com.develop.rs_school.swimmer.repository

import androidx.lifecycle.LiveData
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.util.Result
import com.develop.rs_school.swimmer.di.DataSourceModule
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import com.develop.rs_school.swimmer.util.AgendaStatus
import java.util.*
import javax.inject.Inject

class DataRepository @Inject constructor(
    @DataSourceModule.RemoteDataSource private val networkDataSource: DataSource,
    @DataSourceModule.LocalDataSource private val databaseDataSource: DataSource
) {

    suspend fun refreshLessons(customerId: Int) {
        val lessons: Result<List<Lesson>>  = networkDataSource.getLessons(customerId)
        if (lessons is Result.Success){
            databaseDataSource.deleteLessons() // FIXME id problem
            databaseDataSource.saveLessons(lessons.data)
        }
        if (lessons is Result.Error)
            throw lessons.exception
    }

    val lessons: LiveData<Result<List<Lesson>>> = databaseDataSource.observeLessons()

    suspend fun refreshCustomer(customerId: Int) {
        val customer = networkDataSource.getCustomer(customerId)
        if (customer is Result.Success)
            databaseDataSource.saveCustomer(customer.data)
        if (customer is Result.Error)
            throw customer.exception
    }

    fun getCustomer(customerId: Int): LiveData<Result<Customer>> =
        databaseDataSource.observeCustomer(customerId)

    suspend fun clearData() {
        databaseDataSource.deleteCustomers()
        databaseDataSource.deleteLessons()
    }
}

val fakeLessonsData = listOf<Lesson>(
    Lesson("0", "1", "3", Date(), AgendaStatus.NONE),
    Lesson("1", "2", "3", Date(), AgendaStatus.PLANNED),
    Lesson("2", "3", "3", Date(), AgendaStatus.PREPAID),
    Lesson("3", "4", "3", Date(), AgendaStatus.VISIT_PAID),
    Lesson("4", "5", "3", Date(), AgendaStatus.VISIT_NOT_PAID),
    Lesson("5", "6", "3", Date(), AgendaStatus.MISSED_NOT_PAID),
    Lesson("6", "1", "3", Date(), AgendaStatus.MISSED_FREE),
    Lesson("7", "2", "3", Date(), AgendaStatus.MISSED_PAID),
    Lesson("8", "3", "3", Date(), AgendaStatus.FORGOT),
    Lesson("10", "4", "3", Date(), AgendaStatus.PAUSE),
    Lesson("9", "5", "3", Date(), AgendaStatus.CANCELED)
)