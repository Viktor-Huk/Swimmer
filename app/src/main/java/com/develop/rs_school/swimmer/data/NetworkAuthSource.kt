package com.develop.rs_school.swimmer.data

import android.util.Log
import com.develop.rs_school.swimmer.data.network.SmsApi.sendSmsImpl
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.util.Result
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

class NetworkAuthSource @Inject constructor() : AuthSource {
    override suspend fun authorize(authData: String): Result<Int> {
        return try {
            SwimmerApi.firstAuth()
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

    override suspend fun sendSms(phone: String): String {
        code = nextInt(1000, 9999).toString()
        return sendSmsImpl(code, phone)
    }

    private var code = "1234"

    override fun smsCodeCheck(code: String) = this.code == code
}