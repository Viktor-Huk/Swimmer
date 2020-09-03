package com.develop.rs_school.swimmer.data

interface SessionSource{
    fun getSession(): String
    fun saveSession(session: String)
    fun deleteSession()
}