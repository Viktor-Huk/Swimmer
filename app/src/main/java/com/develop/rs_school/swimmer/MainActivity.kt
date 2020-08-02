package com.develop.rs_school.swimmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.model.*
import com.develop.rs_school.swimmer.network.SwimmerApi
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.NoSuchElementException


class MainActivity : AppCompatActivity() {

    private var customers = MutableLiveData<List<Customer>>()
    private var calendar = MutableLiveData<List<CustomerCalendarItem>>()
    private var lessons = MutableLiveData<List<CustomerLesson>>()

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        testAPI()
        button_signin.setOnClickListener {
            val toolbarActivity = Intent(this, HomeScreenActivity::class.java)
            startActivity(toolbarActivity)
        }
    }

    private fun testAPI() {

        uiScope.launch {
            SwimmerApi.firstAuth()
            customers.value = SwimmerApi.getCustomersImpl()
            //TODO parallel ?
            calendar.value = SwimmerApi.getCustomerCalendarImpl("2376")//name.value ?: "")
            lessons.value = SwimmerApi.getCustomerLesson("2376")//name.value ?: "")

            getCustomerLessonsWithFullInfo("2379")
        }
        customers.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
        calendar.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
        lessons.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
    }

    //TODO need paid_count
    fun getCustomerLessonsWithFullInfo(customerId: String) {
        uiScope.launch {
            val customer = SwimmerApi.getCustomersImpl().first { it.id == customerId }
            //TODO parallel ?
            val lessonInCalendarList = SwimmerApi.getCustomerCalendarImpl(customerId).sortedBy { it.date }
            val lessonList = SwimmerApi.getCustomerLesson(customerId).sortedByDescending { it.date }

            val resultList = mutableListOf<CustomerLessonWithAgenda>()
            var paidLessonInFuture = customer.paid_lesson
            val notPaidLessonIdsInHistory =
                if (paidLessonInFuture < 0)
                    lessonList.map { it.id }.subList(0, -paidLessonInFuture)
                else listOf()
            for (item in lessonInCalendarList) {

                val lessonWithStatus = CustomerLessonWithAgenda(
                    id = item.id,
                    type = item.type,
                    date = item.date,
                    duration = item.duration
                )
                if (item.status == "1") {
                    if (item.date != null && item.date > Date())
                        lessonWithStatus.agendaStatus = AgendaStatus.PLANNED
                    else
                        lessonWithStatus.agendaStatus = AgendaStatus.FORGOT
                    //TODO hack
                    if (lessonWithStatus.agendaStatus == AgendaStatus.PLANNED && paidLessonInFuture > 0) {
                        lessonWithStatus.agendaStatus = AgendaStatus.PREPAID
                        paidLessonInFuture--
                    }
                }
                if (item.status == "2") {
                    lessonWithStatus.agendaStatus = AgendaStatus.CANCELED
                }
                if (item.status == "3") {
                    val lessonDetail = lessonList.first { it.id == item.id }
                    if (lessonDetail.isAttend == 1) {
                        lessonWithStatus.agendaStatus = AgendaStatus.VISIT_PAID
                        if(notPaidLessonIdsInHistory.contains(item.id))
                            lessonWithStatus.agendaStatus = AgendaStatus.VISIT_NOT_PAID
                    }
                    if (lessonDetail.isAttend == 0) {
                        lessonWithStatus.agendaStatus = AgendaStatus.MISSED_PAID
                        if(notPaidLessonIdsInHistory.contains(item.id))
                            lessonWithStatus.agendaStatus = AgendaStatus.MISSED_NOT_PAID
                    }
                    if (lessonDetail.isAttend == 0 && lessonDetail.price == "0.00") {
                        lessonWithStatus.agendaStatus = AgendaStatus.MISSED_FREE
                    }
                }

                resultList.add(lessonWithStatus)
            }
        }
    }

    private fun auth(authData: String) {
        uiScope.launch {
            SwimmerApi.firstAuth()
            try {
                val res = SwimmerApi.getCustomersImpl().first { it.phone.contains(authData) }
                Log.d("1", res.name)
            } catch (e: NoSuchElementException) {
                Log.d("1", "authError")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
