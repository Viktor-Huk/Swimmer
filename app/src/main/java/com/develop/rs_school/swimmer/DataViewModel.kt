package com.develop.rs_school.swimmer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.rs_school.swimmer.model.Customer
import com.develop.rs_school.swimmer.model.CustomerLessonWithAgenda
import com.develop.rs_school.swimmer.network.SwimmerApi.getCustomersImpl
import com.develop.rs_school.swimmer.network.getCustomerLessonsWithFullInfo
import kotlinx.coroutines.launch

//TODO fabric
class DataViewModel : ViewModel() {

    private val _lessons = MutableLiveData<List<CustomerLessonWithAgenda>>()
    val lessons: LiveData<List<CustomerLessonWithAgenda>> get() = _lessons

    private val _profile = MutableLiveData<Customer>()
    val profile: LiveData<Customer> get() = _profile

    init {
        viewModelScope.launch {
            _lessons.value = getCustomerLessonsWithFullInfo("2376")
            _profile.value = getCustomersImpl().first { it.id == "2376"}
        }
    }
}