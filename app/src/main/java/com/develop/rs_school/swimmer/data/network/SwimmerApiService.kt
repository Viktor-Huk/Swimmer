package com.develop.rs_school.swimmer.data.network

import com.develop.rs_school.swimmer.data.network.dto.AuthObject
import com.develop.rs_school.swimmer.data.network.dto.Customer
import com.develop.rs_school.swimmer.data.network.dto.CustomerCalendar
import com.develop.rs_school.swimmer.data.network.dto.CustomerFilterObject
import com.develop.rs_school.swimmer.data.network.dto.CustomerList
import com.develop.rs_school.swimmer.data.network.dto.Lesson
import com.develop.rs_school.swimmer.data.network.dto.LessonFilterObject
import com.develop.rs_school.swimmer.data.network.dto.LessonList
import com.develop.rs_school.swimmer.data.network.dto.Tariff
import com.develop.rs_school.swimmer.data.network.dto.TariffList
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
        @Header("X-ALFACRM-TOKEN") token: String,
        @Body customerFilterObject: CustomerFilterObject
    ): Response<CustomerList>

    @POST("$BRANCH_ID/lesson/index")
    suspend fun getLessons(
        @Header("X-ALFACRM-TOKEN") token: String,
        @Body lessonFilterObject: LessonFilterObject
    ): Response<LessonList>

    @POST("$BRANCH_ID/calendar/customer")
    suspend fun getCustomerCalendar(
        @Header("X-ALFACRM-TOKEN") token: String,
        @Query(value = "id") id: String,
        @Query(value = "date1") dateFrom: Date,
        @Query(value = "date2") dateTo: Date
    ): Response<List<CustomerCalendar>>

    @POST("$BRANCH_ID/customer-tariff/index")
    suspend fun getCustomerTariff(
        @Header("X-ALFACRM-TOKEN") token: String,
        @Query(value = "customer_id") id: Int
    ): Response<TariffList>
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
    suspend fun getAllCustomers(): List<Customer> {
        val resultList = mutableListOf<Customer>()
        var page = 0
        do {
            val customerList = getCustomersImpl(page)
            page++
            resultList.addAll(customerList)
        } while (customerList.isNotEmpty())
        return resultList
    }

    private suspend fun getCustomersImpl(page: Int = 0): List<Customer> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomers(token, CustomerFilterObject(page))
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == tokenErrorCode -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomers(token, CustomerFilterObject(page)).body()?.items
                        ?: listOf()
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

    suspend fun getAllLessons(status: Int = lessonStatusForHistory): List<Lesson> {
        val resultList = mutableListOf<Lesson>()
        var page = 0
        do {
            val lessonList = getLessonsImpl(status, page)
            page++
            resultList.addAll(lessonList)
        } while (lessonList.isNotEmpty())
        return resultList
    }

    private suspend fun getLessonsImpl(
        status: Int = lessonStatusForHistory,
        page: Int = 0
    ): List<Lesson> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getLessons(token, LessonFilterObject(status, page))
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == tokenErrorCode -> {
                    getAuthTokenImpl()
                    retrofitService.getLessons(token, LessonFilterObject(status, page))
                        .body()?.items ?: listOf()
                }
                else -> listOf()
            }
        }
    }

    // TODO make generic method!
    // FIXME paging dont work with tariff in API!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    suspend fun getAllTariff(customerId: Int): List<Tariff> {
        val resultList = mutableListOf<Tariff>()
        var page = 0
        //do {
            val tariffList = getTariffImpl(customerId, page)
            page++
            resultList.addAll(tariffList)
        //} while (tariffList.isNotEmpty())
        return resultList
    }

    private suspend fun getTariffImpl(customerId: Int, page: Int = 0): List<Tariff> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomerTariff(token, customerId)
            when {
                response.isSuccessful -> response.body()?.items ?: listOf()
                response.code() == tokenErrorCode -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomerTariff(token, customerId).body()?.items
                        ?: listOf()
                }
                else -> listOf()
            }
        }
    }
}