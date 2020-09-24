package com.develop.rs_school.swimmer.data.network

import com.develop.rs_school.swimmer.data.network.dto.AuthObject
import com.develop.rs_school.swimmer.data.network.dto.Customer
import com.develop.rs_school.swimmer.data.network.dto.CustomerCalendar
import com.develop.rs_school.swimmer.data.network.dto.CustomerList
import com.develop.rs_school.swimmer.data.network.dto.Lesson
import com.develop.rs_school.swimmer.data.network.dto.LessonList
import com.develop.rs_school.swimmer.data.network.dto.LessonStatusObject
import com.develop.rs_school.swimmer.data.network.dto.TokenObject
import com.develop.rs_school.swimmer.util.getDateDotFormat
import com.develop.rs_school.swimmer.util.getDateWithOffset
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

private const val BASE_URL = "https://mevis.s20.online/v2api/"
private const val BRANCH_ID = "2"

interface SwimmerApiService {
    @POST("auth/login")
    suspend fun getAuthToken(
        @Body authObject: AuthObject
    ): TokenObject

    @POST("$BRANCH_ID/customer/index")
    suspend fun getCustomers(
        @Header("X-ALFACRM-TOKEN") token: String
    ): Response<CustomerList>

    @POST("$BRANCH_ID/lesson/index")
    suspend fun getLessons(
        @Header("X-ALFACRM-TOKEN") token: String,
        @Body statusObject: LessonStatusObject
    ): Response<LessonList>

    @POST("$BRANCH_ID/calendar/customer")
    suspend fun getCustomerCalendar(
        @Header("X-ALFACRM-TOKEN") token: String,
        @Query(value = "id") id: String,
        @Query(value = "date1") dateFrom: Date,
        @Query(value = "date2") dateTo: Date
    ): Response<List<CustomerCalendar>>
}

// TODO move to another file
object SwimmerApi {
    private var token: String = ""
    private const val lessonStatusForHistory = 3
    private const val tokenErrorCode = 403
    private const val defaultDateIntervalStart = -182
    private const val defaultDateIntervalEnd = 182

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(CustomDateTimeAdapter())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    private val retrofitService: SwimmerApiService = retrofit.create(SwimmerApiService::class.java)

    // TODO make one method for auth
    private suspend fun getAuthTokenImpl() {
        val auth = AuthObject()
        token = retrofitService.getAuthToken(auth).token
    }

    suspend fun firstAuth() {
        withContext(Dispatchers.IO) {
            getAuthTokenImpl()
        }
    }

    // TODO make fabric method
    suspend fun getCustomersImpl(): List<Customer> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomers(token)
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == tokenErrorCode -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomers(token).body()?.items ?: listOf()
                }
                else -> listOf()
            }
        }
    }

    suspend fun getCustomerCalendarImpl(
        customerId: String,
        dateFrom: Date = getDateDotFormat(getDateWithOffset(defaultDateIntervalStart)),
        dateTo: Date = getDateDotFormat(getDateWithOffset(defaultDateIntervalEnd))
    ): List<CustomerCalendar> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomerCalendar(token, customerId, dateFrom, dateTo)
            when {
                response.isSuccessful -> response.body() ?: listOf()
                response.code() == tokenErrorCode -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomerCalendar(token, customerId, dateFrom, dateTo).body()
                        ?: listOf()
                }
                else -> listOf()
            }
        }
    }

    suspend fun getLessonsImpl(status: Int = lessonStatusForHistory): List<Lesson> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getLessons(token, LessonStatusObject(status.toString()))
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == tokenErrorCode -> {
                    getAuthTokenImpl()
                    retrofitService.getLessons(token, LessonStatusObject(status.toString()))
                        .body()?.items ?: listOf()
                }
                else -> listOf()
            }
        }
    }
}