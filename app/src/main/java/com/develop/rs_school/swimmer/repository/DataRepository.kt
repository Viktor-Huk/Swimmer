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
        val l = networkDataSource.getLessons(customerId)
        if (l is Result.Success)
            databaseDataSource.saveLessons(l.data)
        if (l is Result.Error)
            throw l.exception
    }

    val lessons: LiveData<Result<List<Lesson>>> = databaseDataSource.observeLessons()

    suspend fun refreshCustomer(customerId: String) {
        val c = networkDataSource.getCustomer(customerId.toInt())
        if (c is Result.Success)
            databaseDataSource.saveCustomer(c.data)
        if (c is Result.Error)
            throw c.exception
    }

    fun getCustomer(customerId: String): LiveData<Result<Customer>> =
        databaseDataSource.observeCustomer(customerId.toInt())

    suspend fun clearData() {
        databaseDataSource.deleteCustomers()
        databaseDataSource.deleteLessons()
    }
}