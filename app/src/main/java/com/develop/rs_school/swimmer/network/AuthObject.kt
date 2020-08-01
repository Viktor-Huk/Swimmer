package com.develop.rs_school.swimmer.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AuthObject(val email: String = "luba200296.lv@gmail.com", val api_key: String = "02bb1557-d34d-11ea-a443-ac1f6b478310") : Parcelable

@Parcelize
data class TokenObject(val token: String) : Parcelable