package com.develop.rs_school.swimmer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.develop.rs_school.swimmer.model.Customer

class RecyclerViewAdapter : RecyclerView.Adapter<CustomViewHolder>(){

    companion object {
        var list = ArrayList<Customer>()
    }

    fun setListData(data: ArrayList<Customer>?) {
        if (data != null) {
            data.forEach {
                list.add(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_raw, parent, false)
        return CustomViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //TODO: add logic in according with caroutines and moshi realization
        //LoadService()
        //            .load(holder.image, list[position].url, holder.image, true, circleSize)
    }

}