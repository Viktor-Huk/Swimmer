package com.develop.rs_school.swimmer.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://mevis.s20.online/v2api/"

//TODO 15 minutes !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
interface SwimmerApiService{
    @POST("auth/login")
    suspend fun getAuthToken(
        //@HeaderMap headers: Map<String, String>,
        @Body authObject: AuthObject
    ): TokenObject

    @POST("2/customer/index")
    suspend fun getCustomers(
        @Header("X-ALFACRM-TOKEN") token: String
    ): CustomerList
}


object SwimmerApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    val retrofitService: SwimmerApiService = retrofit.create(SwimmerApiService::class.java)

    suspend fun getAuthTokenImpl(): String {
        return withContext(Dispatchers.IO){
            val auth = AuthObject()
            retrofitService.getAuthToken(auth).token
        }
    }

    suspend fun getCustomerNameImpl(token: String): String {
        return withContext(Dispatchers.IO){
            retrofitService.getCustomers(token).items[3].name
        }
    }
}