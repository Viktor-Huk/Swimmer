package com.develop.rs_school.swimmer.presentation.main.viewModels

import android.content.Context
import androidx.lifecycle.*
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.SingleLiveEvent
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.data.SessionSource
//import com.develop.rs_school.swimmer.di.ActivityScope
import com.develop.rs_school.swimmer.repository.DataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class MainViewModel @Inject constructor(private val dataRepository: DataRepository, context: Context, var sessionSource: SessionSource) : ViewModel() {//FIXME context

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showError = SingleLiveEvent<Boolean>()
    val showError: SingleLiveEvent<Boolean>
        get() = _showError

    private var customerId: String = sessionSource.getSession()

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
            }
            catch (e: Exception){
                _showError.value = true
            }
            finally {
                _dataLoading.value = false
            }
        }
    }

    fun deleteData(){
        viewModelScope.launch {
            dataRepository.clearData()
        }
    }

    fun deleteSession(){
        sessionSource.deleteSession()
    }
}