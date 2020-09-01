package com.develop.rs_school.swimmer.data

import androidx.lifecycle.LiveData
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson

interface DataSource {
    fun observeLessons(): LiveData<Result<List<Lesson>>>
    suspend fun getLessons(customerId: String): Result<List<Lesson>>
    suspend fun saveLessons(lessons: List<Lesson>)
    suspend fun deleteLessons()

    fun observeCustomer(customerId: Int): LiveData<Result<Customer>>
    suspend fun getCustomer(customerId: Int): Result<Customer>
    suspend fun saveCustomer(customer: Customer)
    suspend fun deleteCustomers()
}