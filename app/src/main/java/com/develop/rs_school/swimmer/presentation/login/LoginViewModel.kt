package com.develop.rs_school.swimmer.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.rs_school.swimmer.util.SingleLiveEvent
import com.develop.rs_school.swimmer.data.AuthSource
import com.develop.rs_school.swimmer.data.SessionSource
import com.develop.rs_school.swimmer.di.AuthSourceModule
import com.develop.rs_school.swimmer.util.Result
import com.develop.rs_school.swimmer.util.getPhoneNumber
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private var sessionSource: SessionSource,
    @AuthSourceModule.NetworkWithoutSms private var authSource: AuthSource
) : ViewModel() {

    private val _showError = SingleLiveEvent<Boolean>()
    val showError: SingleLiveEvent<Boolean>
        get() = _showError

    private val _goToProfile = SingleLiveEvent<Boolean>()
    val goToProfile: SingleLiveEvent<Boolean>
        get() = _goToProfile

    private val _showCodeBar = MutableLiveData<Boolean>()
    val showCodeBar: LiveData<Boolean> = _showCodeBar

    private lateinit var _errorString: String
    val errorString: String
        get() = _errorString

    private fun saveSession(session: Int) {
        sessionSource.saveSession(session)
    }

    private fun getSession(): Int {
        return sessionSource.getSession()
    }

    init {
        if (getSession() != 0) {
            _goToProfile.value = true
        }
    }

    fun sendCodeInSms(phone: String) {
        if(validatePhone(phone))
            viewModelScope.launch {
                try {
                    val smsStatus = authSource.sendSms(getPhoneNumber(phone))
                    Log.d("1", smsStatus)
                    if (smsStatus == "success")
                        _showCodeBar.value = true
                    else {
                        _errorString = smsStatus
                        _showError.value = true
                    }
                } catch (e: Exception) {
                    _errorString = "Network error"
                    _showError.value = true
                }
            }
        else{
            _errorString = "Phone number format error"
            _showError.value = true
        }
    }

    private companion object {
        private const val belPhoneLength = 17
        private const val codeLength = 4
    }
    private fun validatePhone(phone: String) = phone.length == belPhoneLength
    private fun validateSmsCode(code: String) = code.length == codeLength

    fun loginAttempt(phone: String, code: String) {
        if (!smsCodeCheck(code)) {
            _errorString = "Sms code invalid"
            _showError.value = true
            _showCodeBar.value = false
        } else
            viewModelScope.launch {
                // FIXME val t : Result<Int> = Result.Success(2376)
                when (val authApiStatus =
                    authSource.authorize(phone)) { // FIXME debug authSource.authorize(phone) {
                    is Result.Success -> {
                        saveSession(authApiStatus.data)
                        _goToProfile.value = true
                    }
                    is Result.Error -> {
                        _errorString = "Some error"
                        if (authApiStatus.exception is NoSuchElementException)
                            _errorString = "Incorrect login data. Try again!"
                        if (authApiStatus.exception is UnknownHostException)
                            _errorString =
                                "Network error" // getString(R.string.error_network_message)
                        _showError.value = true
                        _showCodeBar.value = false
                    }
                }
            }
    }

    private fun smsCodeCheck(code: String) = authSource.smsCodeCheck(code) && validateSmsCode(code)
}