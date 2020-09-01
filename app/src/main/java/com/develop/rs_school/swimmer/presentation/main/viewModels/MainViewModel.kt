package com.develop.rs_school.swimmer.presentation.main.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.develop.rs_school.swimmer.SingleLiveEvent
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.repository.DataRepository
import kotlinx.coroutines.launch

class MainViewModel(private val customerId: String, app: Application) : ViewModel() {

    private val dataRepository = DataRepository.getRepository(app)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showError = SingleLiveEvent<Boolean>()
    val showError: SingleLiveEvent<Boolean>
        get() = _showError

    init {
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


class DataViewModelFactory(private val customerId: String, val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(customerId, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}