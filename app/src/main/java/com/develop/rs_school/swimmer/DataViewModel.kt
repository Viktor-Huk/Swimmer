package com.develop.rs_school.swimmer

import androidx.lifecycle.*
import com.develop.rs_school.swimmer.model.AgendaStatus
import com.develop.rs_school.swimmer.model.Customer
import com.develop.rs_school.swimmer.model.CustomerLessonWithAgenda
import com.develop.rs_school.swimmer.data.network.SwimmerApi.getCustomersImpl
import com.develop.rs_school.swimmer.data.network.getCustomerLessonsWithFullInfo
import kotlinx.coroutines.launch
import java.util.*

class DataViewModel(private val customerId: String) : ViewModel() {

    private val _lessons = MutableLiveData<List<CustomerLessonWithAgenda>>()
    val lessons: LiveData<List<CustomerLessonWithAgenda>> get() = _lessons

    private val _profile = MutableLiveData<Customer>()
    val profile: LiveData<Customer> get() = _profile

    init {
        updateData()
    }

    fun updateData(){
        viewModelScope.launch {
            val lessons = getCustomerLessonsWithFullInfo(customerId)
            //FIXME add 2 type holder after
            val currentMoment = CustomerLessonWithAgenda("","", Date(),"", AgendaStatus.NONE)
            lessons.add(currentMoment)
            lessons.sortBy { it.date }
            _lessons.value = lessons
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