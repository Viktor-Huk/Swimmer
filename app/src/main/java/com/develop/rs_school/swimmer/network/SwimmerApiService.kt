package com.develop.rs_school.swimmer.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://mevis.s20.online/v2api/"
//var API_KEY = "3418ec30b47a463788dc41c738f919db"

interface SwimmerApiService{
    @POST("auth/login")
    suspend fun getAuthToken(
        //@HeaderMap headers: Map<String, String>,
        @Body authObject: AuthObject
    ): TokenObject
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
}