package com.develop.rs_school.swimmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.model.CustomerCalendarItem
import com.develop.rs_school.swimmer.model.Lesson
import com.develop.rs_school.swimmer.network.SwimmerApi
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private var name = MutableLiveData<String>()
    private var calendar = MutableLiveData<List<CustomerCalendarItem>>()
    private var lessons = MutableLiveData<List<Lesson>>()

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
            name.value = SwimmerApi.getCustomersImpl()[2].id
            calendar.value = SwimmerApi.getCustomerCalendarImpl(name.value ?: "")
            lessons.value = SwimmerApi.getLessonsImpl(2)
        }
        name.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
        calendar.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
        lessons.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
    }

    private fun auth(authData: String){
        uiScope.launch {
            SwimmerApi.firstAuth()
            try {
                val res = SwimmerApi.getCustomersImpl().first { it.phone.contains(authData) }
                Log.d("1", res.name)
            }
            catch (e: NoSuchElementException){
                Log.d("1", "authError")
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
