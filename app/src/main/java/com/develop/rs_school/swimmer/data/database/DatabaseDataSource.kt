package com.develop.rs_school.swimmer.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DatabaseDataSource internal constructor(
    private val lessonDao: LessonDao,
    private val customerDao: CustomerDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DataSource{
    override fun observeLessons(): LiveData<Result<List<Lesson>>> {
        return lessonDao.getLessons().map {
            Result.Success(it.asDomainModel())
        }
    }

    override suspend fun getLessons(): Result<List<Lesson>> {
        TODO("Not yet implemented")
    }

    //withContext(ioDispatcher)
    override fun saveLessons(lessons: List<Lesson>) {
        lessonDao.insertAll(*lessons.asDatabaseModel())
    }

    //withContext(ioDispatcher)
    override fun deleteLessons() {
        lessonDao.deleteAll()
    }

    override fun observeCustomer(customerId: Int): LiveData<Result<Customer>> {
        return customerDao.getCustomer(customerId).map { Result.Success(it.asDomainModel()) }
    }

    override suspend fun getCustomer(customerId: Int): Result<Customer> {
        TODO("Not yet implemented")
    }

    //withContext(ioDispatcher)
    override fun saveCustomer(customer: Customer) {
        customerDao.insertCustomer(customer.asDatabaseModel())
    }

    //withContext(ioDispatcher)
    override fun deleteCustomers() {
        customerDao.deleteAll()
    }

}