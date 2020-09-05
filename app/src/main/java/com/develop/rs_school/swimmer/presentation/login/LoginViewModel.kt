package com.develop.rs_school.swimmer.presentation.login

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.SingleLiveEvent
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.data.SessionSource
import com.develop.rs_school.swimmer.data.network.auth
import com.develop.rs_school.swimmer.presentation.main.ui.MainActivity
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.NoSuchElementException
import javax.inject.Inject

class LoginViewModel @Inject constructor(private var sessionSource: SessionSource) : ViewModel() {

    private val _showError = SingleLiveEvent<Boolean>()
    val showError: SingleLiveEvent<Boolean>
        get() = _showError

    private val _goToProfile = SingleLiveEvent<Boolean>()
    val goToProfile: SingleLiveEvent<Boolean>
        get() = _goToProfile

    private lateinit var _errorString: String
    val errorString: String
        get() = _errorString

    private lateinit var _smsCode: String

    private fun saveSession(session: String) {
        sessionSource.saveSession(session)
    }

    private fun getSession(): String {
        return sessionSource.getSession()
    }

    init {
        if (getSession() != "") {
            _goToProfile.value = true
        }
    }

    fun loginAttempt(phone: String) {
        viewModelScope.launch {

            //val t = SmsApi.sendSmsImpl("3",binding.textInput.text.toString().replace("(","").replace(")","").replace("-","").replace("+",""))
//if(binding.textInput.text.toString()!="")
            when (val authApiStatus = auth(phone)) {
                is Result.Success -> {
                    saveSession(authApiStatus.data)
                    _goToProfile.value = true
                }
                is Result.Error -> {
                    _errorString = "Some error"
                    if(authApiStatus.exception is NoSuchElementException)
                        _errorString = "Incorrect login data. Try again!"
                    if(authApiStatus.exception is UnknownHostException)
                        _errorString = "Network error" //getString(R.string.error_network_message)
                    _showError.value = true
                }
            }
        }
    }

    private fun smsCodeCheck(code: String): Boolean {
        return code == _smsCode
    }
}