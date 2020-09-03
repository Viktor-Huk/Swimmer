package com.develop.rs_school.swimmer.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.data.database.DatabaseDataSource
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.data.network.NetworkDataSource
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//FIXME DI
class DataRepository(
    private val networkDataSource: DataSource,
    private val databaseDataSource: DataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getRepository(app: Application): DataRepository {
            return INSTANCE ?: synchronized(this) {
                DataRepository(
                    NetworkDataSource(),
                    DatabaseDataSource(
                        SwimmerDatabase.getInstance(app).lessonDatabaseDao,//FIXME DI
                        SwimmerDatabase.getInstance(app).customerDatabaseDao
                    )
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

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