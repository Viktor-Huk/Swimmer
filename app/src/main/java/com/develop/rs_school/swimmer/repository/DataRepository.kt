package com.develop.rs_school.swimmer.repository

import androidx.lifecycle.LiveData
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.di.DataSourceModule
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import javax.inject.Inject

class DataRepository @Inject constructor(
    @DataSourceModule.RemoteDataSource private val networkDataSource: DataSource,
    @DataSourceModule.LocalDataSource private val databaseDataSource: DataSource
) {

    suspend fun refreshLessons(customerId: String) {
        val lessons = networkDataSource.getLessons(customerId)
        if (lessons is Result.Success)
            databaseDataSource.saveLessons(lessons.data)
        if (lessons is Result.Error)
            throw lessons.exception
    }

    val lessons: LiveData<Result<List<Lesson>>> = databaseDataSource.observeLessons()

    suspend fun refreshCustomer(customerId: String) {
        val customer = networkDataSource.getCustomer(customerId.toInt())
        if (customer is Result.Success)
            databaseDataSource.saveCustomer(customer.data)
        if (customer is Result.Error)
            throw customer.exception
    }

    fun getCustomer(customerId: String): LiveData<Result<Customer>> =
        databaseDataSource.observeCustomer(customerId.toInt())

    suspend fun clearData() {
        databaseDataSource.deleteCustomers()
        databaseDataSource.deleteLessons()
    }
}