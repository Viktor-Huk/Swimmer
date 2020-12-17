package com.develop.rs_school.swimmer.data

interface SessionSource {
    fun getSession(): CustomerSession
    fun saveSession(session: CustomerSession)
    fun deleteSession()
}

data class CustomerSession(val id: Int, val phone: String)