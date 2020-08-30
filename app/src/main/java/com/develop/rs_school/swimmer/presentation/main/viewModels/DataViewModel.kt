package com.develop.rs_school.swimmer.presentation.main.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.repository.LessonsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataViewModel(private val customerId: String, app: Application) : ViewModel() {

    private val database = SwimmerDatabase.getInstance(app)
    private val lessonsRepository = LessonsRepository(database)

    init {
        updateData()
    }

    val lessons = lessonsRepository.lessons
    val profile = lessonsRepository.getCustomer(customerId)

    fun updateData(){
        viewModelScope.launch {
            lessonsRepository.refreshLessons(customerId)
            lessonsRepository.refreshCustomer(customerId)
        }
    }

    fun deleteData(){
        viewModelScope.launch {
            lessonsRepository.clearData()
        }
    }
}


class DataViewModelFactory(private val customerId: String, val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(customerId, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}