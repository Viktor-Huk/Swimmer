package com.develop.rs_school.swimmer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develop.rs_school.swimmer.network.auth
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

//    private var customers = MutableLiveData<List<Customer>>()
//    private var calendar = MutableLiveData<List<CustomerCalendarItem>>()
//    private var lessons = MutableLiveData<List<CustomerLesson>>()

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        //testAPI()

        button_signin.setOnClickListener {
            uiScope.launch {
                val loginStatus = auth(text_input.text.toString())
                if(loginStatus != ""){
                    val toolbarActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(toolbarActivity)
                }
                else{
                    Toast.makeText(this@LoginActivity, "Incorrect data", Toast.LENGTH_LONG).show()

                    //FIXME for test
                    val toolbarActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(toolbarActivity)
                }
            }
        }
    }

//    private fun testAPI() {
//
//        uiScope.launch {
//            SwimmerApi.firstAuth()
//            customers.value = SwimmerApi.getCustomersImpl()
//            //TODO parallel ?
//            calendar.value = SwimmerApi.getCustomerCalendarImpl("2376")//name.value ?: "")
//            lessons.value = SwimmerApi.getCustomerLesson("2376")//name.value ?: "")
//
//            //getCustomerLessonsWithFullInfo("2379")
//        }
//        customers.observe(this, Observer { newValue ->
//            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
//        })
//        calendar.observe(this, Observer { newValue ->
//            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
//        })
//        lessons.observe(this, Observer { newValue ->
//            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
//        })
//    }

//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) hideSystemUI()
//    }
//
//    private fun hideSystemUI() {
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN)
//    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
