package com.develop.rs_school.swimmer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.account_layout.view.*
import kotlinx.android.synthetic.main.recycler_view_raw.view.*

class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image = view.image
//    val userName = view.name
    val dob = view.dob
//    val balance = view.balance
//    val paid_lesson = view.paid_balance
    val phone = view.phone_field
    val email = view.email_layout
}