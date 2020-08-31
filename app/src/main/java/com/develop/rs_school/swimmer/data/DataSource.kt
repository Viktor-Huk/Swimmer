package com.develop.rs_school.swimmer.data

import androidx.lifecycle.LiveData
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson

interface DataSource {
    fun observeLessons(): LiveData<Result<List<Lesson>>>
    suspend fun getLessons(): Result<List<Lesson>>
    fun saveLessons(lessons: List<Lesson>)
    fun deleteLessons()

    fun observeCustomer(customerId: Int): LiveData<Result<Customer>>
    suspend fun getCustomer(customerId: Int): Result<Customer>
    fun saveCustomer(customer: Customer)
    fun deleteCustomers()
}