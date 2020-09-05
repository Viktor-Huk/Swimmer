package com.develop.rs_school.swimmer.data

import android.content.Context
import com.develop.rs_school.swimmer.R
import javax.inject.Inject

class SharedPrefSessionSource @Inject constructor(val context: Context): SessionSource{

    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref), Context.MODE_PRIVATE)

    override fun getSession(): Int {
        return sharedPreferences.getInt(context.getString(R.string.session_id_pref), 0)
    }

    override fun saveSession(session: Int) {
        with(sharedPreferences.edit()) {
            putInt(context.getString(R.string.session_id_pref), session)
            apply()
        }
    }

    override fun deleteSession() {
        with (sharedPreferences.edit()) {
            remove(context.getString(R.string.session_id_pref))
            commit()
        }
    }
}