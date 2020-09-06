package com.develop.rs_school.swimmer.data

import com.develop.rs_school.swimmer.util.Result

interface AuthSource {
    suspend fun authorize(authData: String): Result<Int>
    suspend fun sendSms(message: String, phone: String): String
}