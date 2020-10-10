package com.develop.rs_school.swimmer.data

import android.util.Log
import com.develop.rs_school.swimmer.data.network.SmsApi
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.util.Result
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

class NetworkAuthSource @Inject constructor(private val swimmerApi: SwimmerApi) : AuthSource {

    private companion object {
        private const val MIN_SMS_CODE = 1000
        private const val MAX_SMS_CODE = 9999
    }

    override suspend fun authorize(authData: String): Result<Int> {
        return try {
            swimmerApi.firstAuth()
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

    override suspend fun sendSms(phone: String): String {
        code = nextInt(MIN_SMS_CODE, MAX_SMS_CODE).toString()
        // FIXME DI SmsApi
        return SmsApi.sendSmsImpl(code, phone)
    }

    private var code = "1234"

    override fun smsCodeCheck(code: String) = this.code == code
}