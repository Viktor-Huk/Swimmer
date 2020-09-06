package com.develop.rs_school.swimmer.data

interface SessionSource {
    fun getSession(): Int
    fun saveSession(session: Int)
    fun deleteSession()
}