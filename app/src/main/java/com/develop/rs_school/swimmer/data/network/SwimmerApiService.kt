package com.develop.rs_school.swimmer.data.network

import com.develop.rs_school.swimmer.data.network.dto.Customer
import com.develop.rs_school.swimmer.data.network.dto.CustomerCalendar
import com.develop.rs_school.swimmer.data.network.dto.CustomerList
import com.develop.rs_school.swimmer.data.network.dto.Lesson
import com.develop.rs_school.swimmer.data.network.dto.LessonList
import com.develop.rs_school.swimmer.data.network.dto.LessonStatusObject
import com.develop.rs_school.swimmer.data.network.dto.PhoneAuthObject
import com.develop.rs_school.swimmer.data.network.dto.Token2Object
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


//private const val BASE_URL = "https://mevis.s20.online/v2api/"
//remote server http://3.137.145.255:8080/v2api/
//local server http://10.0.2.2:8080/v2api/
private const val BASE_URL = "https://morning-cove-66722.herokuapp.com/v2api/"
private const val BRANCH_ID = "2"
private var phoneNumb = ""

interface SwimmerApiService {

    @POST("auth/login")
    suspend fun getAuthBearerToken(
        @Body authObject: PhoneAuthObject
    ): Token2Object

    @POST("$BRANCH_ID/customer/index")
    suspend fun getCustomers(
        @Header("Content-Type") type: String,
        @Header("Authorization") bearer : String
    ): Response<CustomerList>

    @POST("$BRANCH_ID/lesson/index")
    suspend fun getLessons(
//        @Header("X-ALFACRM-TOKEN") token: String,
        @Header("Content-Type") type: String,
        @Header("Authorization") bearer : String,
        @Body statusObject: LessonStatusObject
    ): Response<LessonList>

    @POST("$BRANCH_ID/calendar/customer")
    suspend fun getCustomerCalendar(
//        @Header("X-ALFACRM-TOKEN") token: String,
        @Header("Content-Type") type: String,
        @Header("Authorization") bearer : String,
        @Query(value = "id") id: String,
        @Query(value = "date1") dateFrom: String,//01.01.2020
        @Query(value = "date2") dateTo: String
    ): Response<List<CustomerCalendar>>

}

//TODO move to another file
object SwimmerApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(CustomDateTimeAdapter())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    private val retrofitService: SwimmerApiService = retrofit.create(SwimmerApiService::class.java)

    private var token: String = ""
    private const val lessonStatusForHistory = 3

    //TODO make one method for auth
    private suspend fun getAuthTokenImpl() {
        val auth = PhoneAuthObject(phone = phoneNumb)
        token = retrofitService.getAuthBearerToken(auth).accessToken
    }

    suspend fun firstAuth(auth : String) {
        withContext(Dispatchers.IO) {
            phoneNumb = auth
            getAuthTokenImpl()
        }
    }

    //TODO make fabric method
    suspend fun getCustomersImpl(): List<Customer> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomers("application/json", "Bearer $token")
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == 403 -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomers("application/json", "Bearer $token").body()?.items ?: listOf()
                }
                else -> listOf()
            }
        }
    }

    suspend fun getCustomerCalendarImpl(
        customerId: String,
        dateFrom: String = "01.01.2020",//FIXME
        dateTo: String = "31.12.2020"
    ): List<CustomerCalendar> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomerCalendar("application/json", "Bearer $token", customerId, dateFrom, dateTo)
            when {
                response.isSuccessful -> response.body() ?: listOf()
                response.code() == 403 -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomerCalendar("application/json", "Bearer $token", customerId, dateFrom, dateTo).body()
                        ?: listOf()
                }
                else -> listOf()
            }
        }
    }

    suspend fun getLessonsImpl(status: Int = lessonStatusForHistory): List<Lesson> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getLessons("application/json", "Bearer $token", LessonStatusObject(status.toString()))
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == 403 -> {
                    getAuthTokenImpl()
                    retrofitService.getLessons("application/json", "Bearer $token", LessonStatusObject(status.toString()))
                        .body()?.items ?: listOf()
                }
                else -> listOf()
            }
        }
    }

}
