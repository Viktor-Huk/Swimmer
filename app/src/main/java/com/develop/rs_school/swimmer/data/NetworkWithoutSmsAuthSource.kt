package com.develop.rs_school.swimmer.data

import android.util.Log
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.util.Result
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkWithoutSmsAuthSource @Inject constructor(private val swimmerApi: SwimmerApi) : AuthSource {
    override suspend fun authorize(authData: String): Result<Int> {
        return try {
            // FIXME with server another way
            swimmerApi.firstAuth(authData)
            val res = swimmerApi.getAllCustomers().first { it.phone.contains(authData) }

            Log.d("1", res.name)
            Result.Success(res.id)
        } catch (e: NoSuchElementException) {
            Log.d("1", "authError for $authData")
            Result.Error(e)
        } catch (e: UnknownHostException) {
            Log.d("1", e.toString())
            Result.Error(e)
        } catch (e: Exception) {
            Log.d("1", e.toString())
            Result.Error(e)
        }
    }

    override suspend fun sendSms(phone: String) = "success"

    override fun smsCodeCheck(code: String) = true

    override fun saveAuthData(authData: String){
        swimmerApi.setAuthPhone(authData)
    }
}