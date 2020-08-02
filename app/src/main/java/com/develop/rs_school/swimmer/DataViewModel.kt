package com.develop.rs_school.swimmer

import androidx.lifecycle.*
import com.develop.rs_school.swimmer.model.Customer
import com.develop.rs_school.swimmer.model.CustomerLessonWithAgenda
import com.develop.rs_school.swimmer.network.SwimmerApi.getCustomersImpl
import com.develop.rs_school.swimmer.network.getCustomerLessonsWithFullInfo
import kotlinx.coroutines.launch

//TODO fabric
class DataViewModel(customerId: String) : ViewModel() {

    private val _lessons = MutableLiveData<List<CustomerLessonWithAgenda>>()
    val lessons: LiveData<List<CustomerLessonWithAgenda>> get() = _lessons

    private val _profile = MutableLiveData<Customer>()
    val profile: LiveData<Customer> get() = _profile

    init {
        viewModelScope.launch {
            _lessons.value = getCustomerLessonsWithFullInfo(customerId)
            _profile.value = getCustomersImpl().first { it.id == customerId}
        }
    }
}


class DataViewModelFactory(private val customerId: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(customerId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}