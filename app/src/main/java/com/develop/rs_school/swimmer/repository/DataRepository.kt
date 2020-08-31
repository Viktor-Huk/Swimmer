package com.develop.rs_school.swimmer.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import androidx.room.Room
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.data.database.DatabaseCustomer
import com.develop.rs_school.swimmer.data.database.DatabaseDataSource
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.data.database.asDomainModel
import com.develop.rs_school.swimmer.data.network.NetworkDataSource
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.data.network.asDatabaseModel
import com.develop.rs_school.swimmer.data.network.dto.asDatabaseModel
import com.develop.rs_school.swimmer.data.network.getCustomerLessonsWithFullInfo
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.domain.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//FIXME CustomerID
class DataRepository private constructor(application: Application, customerId: String) {

    private val networkDataSource: DataSource
    private val databaseDataSource: DataSource
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getRepository(app: Application,customerId: String): DataRepository {
            return INSTANCE ?: synchronized(this) {
                DataRepository(app, customerId).also {
                    INSTANCE = it
                }
            }
        }
    }

    init {
        val database = SwimmerDatabase.getInstance(application)
        networkDataSource = NetworkDataSource(customerId)
        databaseDataSource = DatabaseDataSource(database.lessonDatabaseDao, database.customerDatabaseDao)
    }


    suspend fun refreshLessons(customerId: String) {
        withContext(Dispatchers.IO) {
            val l = networkDataSource.getLessons()
            if(l is Result.Success)
                databaseDataSource.saveLessons(l.data)
        }
    }

    val lessons2: LiveData<Result<List<Lesson>>> = databaseDataSource.observeLessons()

    suspend fun refreshCustomer(customerId: String) {
        withContext(Dispatchers.IO) {
            val c =networkDataSource.getCustomer(customerId.toInt())
            if(c is Result.Success)
                databaseDataSource.saveCustomer(c.data)
        }
    }

    fun getCustomer2(customerId: String): LiveData<Result<Customer>> = databaseDataSource.observeCustomer(customerId.toInt())

    suspend fun clearData(){
        withContext(Dispatchers.IO) {
            databaseDataSource.deleteCustomers()
            databaseDataSource.deleteLessons()
        }
    }
}