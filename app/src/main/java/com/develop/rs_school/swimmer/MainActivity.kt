package com.develop.rs_school.swimmer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.network.AuthObject
import com.develop.rs_school.swimmer.network.SwimmerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject



class MainActivity : AppCompatActivity() {

    private var token = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        token.observe(this, Observer { newValue -> Toast.makeText(this, newValue.toString(), Toast.LENGTH_SHORT).show()})

        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main )
        coroutineScope.launch {

//            val headers = HashMap<String, String>()
//            headers["X-ALFACRM-TOKEN"] = "paste AUTHORIZATION value here"
//            headers["KEY_TOKEN"] = "paste TOKEN value here"


            val auth = AuthObject()

            token.value = SwimmerApi.retrofitService.getAuthToken(auth).token
        }
    }
}