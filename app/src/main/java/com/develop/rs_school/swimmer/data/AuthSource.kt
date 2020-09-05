package com.develop.rs_school.swimmer.data

interface AuthSource{
    suspend fun authorize(authData: String): Result<Int>
    suspend fun sendSms(message: String, phone: String) : String
}