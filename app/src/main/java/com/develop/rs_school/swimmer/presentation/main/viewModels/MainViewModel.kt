package com.develop.rs_school.swimmer.presentation.main.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.develop.rs_school.swimmer.SingleLiveEvent
import com.develop.rs_school.swimmer.data.SessionSource
import com.develop.rs_school.swimmer.repository.DataRepository
import com.develop.rs_school.swimmer.util.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private var sessionSource: SessionSource
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showError = SingleLiveEvent<Boolean>()
    val showError: SingleLiveEvent<Boolean>
        get() = _showError

    private val _goToLogin = SingleLiveEvent<Boolean>()
    val goToLogin: SingleLiveEvent<Boolean>
        get() = _goToLogin

    private var customerId: Int = sessionSource.getSession()

    init {
        updateData()
    }

    val lessons = dataRepository.lessons.map { if (it is Result.Success) it.data else listOf() }

    val profile = dataRepository.getCustomer(customerId).map {
        if (it is Result.Success) it.data else null
    }

    fun updateData() {
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                dataRepository.refreshLessons(customerId)
                dataRepository.refreshCustomer(customerId)
            } catch (e: Exception) {
                _showError.value = true
            } finally {
                _dataLoading.value = false
            }
        }
    }

    fun logout() {
        deleteData()
        deleteSession()
        _goToLogin.value = true
    }

    private fun deleteData() {
        viewModelScope.launch {
            dataRepository.clearData()
        }
    }

    private fun deleteSession() {
        sessionSource.deleteSession()
    }
}