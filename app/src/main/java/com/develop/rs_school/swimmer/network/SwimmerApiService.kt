package com.develop.rs_school.swimmer.network

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

private const val BASE_URL = "https://mevis.s20.online/v2api/"

interface SwimmerApiService {
    @POST("auth/login")
    suspend fun getAuthToken(
        //@HeaderMap headers: Map<String, String>,
        @Body authObject: AuthObject
    ): TokenObject

    @POST("2/customer/index")
    suspend fun getCustomers(
        @Header("X-ALFACRM-TOKEN") token: String
    ): Response<CustomerList>
}

object SwimmerApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    private val retrofitService: SwimmerApiService = retrofit.create(SwimmerApiService::class.java)

    private var token: String = ""

    //TODO make one method for auth
    private suspend fun getAuthTokenImpl() {
        val auth = AuthObject()
        token = retrofitService.getAuthToken(auth).token
    }

    suspend fun firstAuth() {
        withContext(Dispatchers.IO) {
            val auth = AuthObject()
            token = retrofitService.getAuthToken(auth).token
        }
    }

    suspend fun getCustomersImpl(): List<Customer> {
        return withContext(Dispatchers.IO) {
            val response = retrofitService.getCustomers(token)
            when {
                response.isSuccessful -> response.body()!!.items
                response.code() == 403 -> {
                    getAuthTokenImpl()
                    retrofitService.getCustomers(token).body()!!.items
                }
                else -> listOf()
            }
        }
    }
}