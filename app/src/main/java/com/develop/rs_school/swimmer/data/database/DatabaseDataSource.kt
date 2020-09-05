package com.develop.rs_school.swimmer.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseDataSource @Inject internal constructor(
    private val lessonDao: LessonDao,
    private val customerDao: CustomerDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DataSource{

    override fun observeLessons(): LiveData<Result<List<Lesson>>> {
        return lessonDao.getLessons().map {
            Result.Success(it.asDomainModel())
        }
    }

    override suspend fun getLessons(customerId: Int): Result<List<Lesson>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveLessons(lessons: List<Lesson>) = withContext(ioDispatcher){
        lessonDao.insertAll(*lessons.asDatabaseModel())
    }

    override suspend fun deleteLessons()  = withContext(ioDispatcher){
        lessonDao.deleteAll()
    }

    override fun observeCustomer(customerId: Int): LiveData<Result<Customer>> {
        return customerDao.getCustomer(customerId).map {
            if (it != null)
                Result.Success(it.asDomainModel())
            else
                Result.Error(Exception("Customer not found!"))
        }
    }

    override suspend fun getCustomer(customerId: Int): Result<Customer> {
        TODO("Not yet implemented")
    }

    override suspend fun saveCustomer(customer: Customer)  = withContext(ioDispatcher){
        customerDao.insertCustomer(customer.asDatabaseModel())
    }

    override suspend fun deleteCustomers()  = withContext(ioDispatcher){
        customerDao.deleteAll()
    }

}