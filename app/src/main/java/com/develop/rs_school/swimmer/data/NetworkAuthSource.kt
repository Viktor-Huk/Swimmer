package com.develop.rs_school.swimmer.data

import android.util.Log
import com.develop.rs_school.swimmer.util.Result
import com.develop.rs_school.swimmer.data.network.SmsApi.sendSmsImpl
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import java.net.UnknownHostException
import java.util.NoSuchElementException
import javax.inject.Inject

class NetworkAuthSource @Inject constructor(): AuthSource{
    override suspend fun authorize(authData: String): Result<Int> {
        return try {
            SwimmerApi.firstAuth(authData)
            val res = SwimmerApi.getCustomersImpl().first { it.phone.contains(authData) }
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

    override suspend fun sendSms(message: String, phone: String): String {
        return sendSmsImpl(message, phone)
    }
}