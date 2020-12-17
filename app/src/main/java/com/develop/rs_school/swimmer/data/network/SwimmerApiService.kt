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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Date

private const val BRANCH_ID = "2"

interface SwimmerApiService {
    @POST("auth/login")
    suspend fun getAuthToken(
        @Body authObject: AuthObject
    ): TokenObject

    @POST("$BRANCH_ID/customer/index")
    suspend fun getCustomers(
        @Header("Authorization") bearer: String,
        @Body customerFilterObject: CustomerFilterObject,
        @Header("Content-Type") type: String = "application/json"
    ): Response<CustomerList>

    @POST("$BRANCH_ID/lesson/index")
    suspend fun getLessons(
        @Header("Authorization") bearer: String,
        @Body lessonFilterObject: LessonFilterObject,
        @Header("Content-Type") type: String = "application/json"
    ): Response<LessonList>

    @POST("$BRANCH_ID/calendar/customer")
    suspend fun getCustomerCalendar(
        @Header("Authorization") bearer: String,
        @Query(value = "id") id: String,
        @Query(value = "date1") dateFrom: String,
        @Query(value = "date2") dateTo: String,
        @Header("Content-Type") type: String = "application/json"
    ): Response<List<CustomerCalendar>>

    @POST("$BRANCH_ID/customer-tariff/index")
    suspend fun getCustomerTariff(
        @Header("Authorization") bearer: String,
        @Query(value = "customer_id") id: Int,
        @Header("Content-Type") type: String = "application/json"
    ): Response<TariffList>
}

// TODO move to another file
class SwimmerApi(private val retrofitService: SwimmerApiService) {
    // TODO to constants
    private var token: String = ""
        get() = "Bearer $field"
    val lessonStatusForHistory = 3
    private val tokenErrorCode = 401  // FIXME maybe 403 problem in server
    private val defaultDateIntervalStart = -182
    private val defaultDateIntervalEnd = 182

    private var phoneNumb = "" // FIXME problem with reAuth getAuthTokenImpl()

    // TODO make one method for auth
    private suspend fun getAuthTokenImpl() {
        val auth = AuthObject(phoneNumb)
        token = retrofitService.getAuthToken(auth).accessToken
    }

    suspend fun firstAuth(auth: String) {
        withContext(Dispatchers.IO) {
            setAuthPhone(auth)
            getAuthTokenImpl()
        }
    }

    fun setAuthPhone(authPhone: String){
        phoneNumb = authPhone
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

    suspend fun getCustomerById(customerId: Int): Customer {
        return getCustomersImpl(customerId = customerId).single()
    }

    private suspend fun getCustomersImpl(page: Int = 0, customerId: Int = 0): List<Customer> {
        return withContext(Dispatchers.IO) {
            getDataFromApi {
                retrofitService.getCustomers(
                    token,
                    CustomerFilterObject(page, customerId)
                )
            }
        }?.items ?: listOf()
    }

    suspend fun getCustomerCalendarImpl(
        customerId: String,
        dateFrom: Date = getDateDotFormat(getDateWithOffset(defaultDateIntervalStart)),
        dateTo: Date = getDateDotFormat(getDateWithOffset(defaultDateIntervalEnd))
    ): List<CustomerCalendar> {
        return withContext(Dispatchers.IO) {
            getDataFromApi {
                retrofitService.getCustomerCalendar(
                    token,
                    customerId,
                    SimpleDateFormat("dd.MM.yyyy").format(dateFrom),
                    SimpleDateFormat("dd.MM.yyyy").format(dateTo)
                )
            }
        } ?: listOf()
    }

    suspend fun getAllLessons(
        status: Int = lessonStatusForHistory,
        lessonsIds: List<String> = listOf()
    ): List<Lesson> {
        val resultList = mutableListOf<Lesson>()
        var page = 0
        do {
            val lessonList = getLessonsImpl(status, page, lessonsIds)
            page++
            resultList.addAll(lessonList)
        } while (lessonList.isNotEmpty())
        return resultList
    }

    private suspend fun getLessonsImpl(
        status: Int = lessonStatusForHistory,
        page: Int = 0,
        ids: List<String> = listOf()
    ): List<Lesson> {
        return withContext(Dispatchers.IO) {
            getDataFromApi {
                retrofitService.getLessons(
                    token,
                    LessonFilterObject(status, page, ids)
                )
            }
        }?.items ?: listOf()
    }

    // FIXME paging dont work with tariff in API!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    suspend fun getAllTariff(customerId: Int): List<Tariff> {
        val resultList = mutableListOf<Tariff>()
        var page = 0
        // do {
        val tariffList = getTariffImpl(customerId, page)
        page++
        resultList.addAll(tariffList)
        // } while (tariffList.isNotEmpty())
        return resultList
    }

    private suspend fun getTariffImpl(customerId: Int, page: Int = 0): List<Tariff> {
        val tariffList = withContext(Dispatchers.IO) {
            getDataFromApi { retrofitService.getCustomerTariff(token, customerId) }
        }
        return tariffList?.items ?: listOf()
    }

    private suspend inline fun <T> getDataFromApi(apiCall: () -> Response<T>): T? {
        val apiResponse = apiCall()
        return when {
            apiResponse.isSuccessful -> apiResponse.body()
            apiResponse.code() == 403 -> {
                getAuthTokenImpl()
                apiCall().body()
            }
            else -> null
        }
    }
}