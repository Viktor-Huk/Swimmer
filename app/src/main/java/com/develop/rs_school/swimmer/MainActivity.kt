package com.develop.rs_school.swimmer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.network.AuthObject
import com.develop.rs_school.swimmer.network.SwimmerApi
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private var name = MutableLiveData<String>()

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testAPI()
    }

    private fun testAPI() {
        uiScope.launch {
            SwimmerApi.firstAuth()
            name.value = SwimmerApi.getCustomersImpl()[3].toString()
        }
        name.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
