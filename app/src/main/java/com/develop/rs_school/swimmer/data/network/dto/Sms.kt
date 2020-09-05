package com.develop.rs_school.swimmer.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Sms (
    @Json(name = "sms_id") val smsId: String?,
    @Json(name = "status") val smsStatus: String?,
    @Json(name = "error") val error: String?
) : Parcelable
