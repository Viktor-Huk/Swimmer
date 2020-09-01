package com.develop.rs_school.swimmer.data.network

import androidx.lifecycle.LiveData
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.data.network.dto.asDomainModel
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkDataSource(val customerId: String): DataSource {
    override fun observeLessons(): LiveData<Result<List<Lesson>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessons(): Result<List<Lesson>> =
        withContext(Dispatchers.IO) {
            val networkLessons = getCustomerLessonsWithFullInfo(customerId)
            return@withContext Result.Success(networkLessons.asDomainModel())
        }


    override suspend fun saveLessons(lessons: List<Lesson>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLessons() {
        TODO("Not yet implemented")
    }

    override fun observeCustomer(customerId: Int): LiveData<Result<Customer>> {
        TODO("Not yet implemented")
    }

    //FIXME SwimmerApi - DI
    override suspend fun getCustomer(customerId: Int): Result<Customer> =
        withContext(Dispatchers.IO) {
            return@withContext Result.Success(SwimmerApi.getCustomersImpl().first { it.id == customerId.toString() }.asDomainModel())
        }

    override suspend fun saveCustomer(customer: Customer) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCustomers() {
        TODO("Not yet implemented")
    }
}