package com.develop.rs_school.swimmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.network.SwimmerApi
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private var token = MutableLiveData<String>()
    private var name = MutableLiveData<String>()

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
        token.observe(
            this,
            Observer { newValue ->
                Toast.makeText(this, newValue.toString(), Toast.LENGTH_SHORT).show()
                uiScope.launch {
                    name.value = SwimmerApi.getCustomerNameImpl(newValue)
                }
            })

        name.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })

        uiScope.launch {
            token.value = SwimmerApi.getAuthTokenImpl()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
