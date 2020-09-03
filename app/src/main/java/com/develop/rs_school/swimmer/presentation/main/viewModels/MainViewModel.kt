package com.develop.rs_school.swimmer.presentation.main.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.SingleLiveEvent
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.domain.Customer
import com.develop.rs_school.swimmer.repository.DataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(private val dataRepository: DataRepository, context: Context) : ViewModel() {//FIXME context

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showError = SingleLiveEvent<Boolean>()
    val showError: SingleLiveEvent<Boolean>
        get() = _showError

    private var customerId: String

    init {
        customerId = context.getSharedPreferences(context.getString(R.string.app_pref), Context.MODE_PRIVATE).getString(context.getString(R.string.sessionId), "") ?: ""
        updateData()
    }

    val lessons = dataRepository.lessons
    val profile = dataRepository.getCustomer(customerId).map {
        if(it is Result.Success) it.data else null
    }

    fun updateData(){
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                dataRepository.refreshLessons(customerId)
                dataRepository.refreshCustomer(customerId)
                _dataLoading.value = false
            }
            catch (e: Exception){
                _showError.value = true
                _dataLoading.value = false
            }
        }
    }

    fun deleteData(){
        viewModelScope.launch {
            dataRepository.clearData()
        }
    }
}

/*
class DataViewModelFactory @Inject constructor(private val dataRepository: DataRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/