package com.develop.rs_school.swimmer.data.network

import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.data.network.dto.Sms
import com.develop.rs_school.swimmer.data.network.dto.asDomainModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface SmsApiService {
    @GET("sendQuickSMS")
    suspend fun sendSms(
        @Query(value = "token") token: String = "5cfa8865baae84b79f9e3aa08397982a",
        @Query(value = "message") message: String,
        @Query(value = "phone") phone: String
    ): Response<Sms>
}

private const val BASE_URL_SMS = "https://app.sms.by/api/v1/"

object SmsApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(CustomDateTimeAdapter())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL_SMS)
        .build()
    private val retrofitService: SmsApiService = retrofit.create(SmsApiService::class.java)

    suspend fun sendSmsImpl(message: String, phone: String): String {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.sendSms(message = message, phone = phone)
            when {
                response.isSuccessful -> {
                    val respBody = response.body()
                    when {
                        //respBody == null ->  "some error"
                        respBody?.smsStatus != null -> "success"
                        respBody?.error != null ->  respBody.error
                        else -> "some error"
                    }
                }
                else -> response.code().toString()
            }
        }
    }
}