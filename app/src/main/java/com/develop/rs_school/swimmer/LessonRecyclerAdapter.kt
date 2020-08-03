package com.develop.rs_school.swimmer


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.develop.rs_school.swimmer.databinding.RecyclerViewRawBinding
import com.develop.rs_school.swimmer.model.AgendaStatus
import com.develop.rs_school.swimmer.model.CustomerLessonWithAgenda
import java.text.SimpleDateFormat

class LessonRecyclerAdapter(private val itemClickListener: LessonRecyclerItemListener) :
    ListAdapter<CustomerLessonWithAgenda, LessonRecyclerAdapter.ViewHolder>(LessonDiffUtilCallback()) {

    inner class ViewHolder(private val itemBinding: RecyclerViewRawBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                //getItem(adapterPosition)?.let { itemClickListener.onClick(it) }
            }
        }

        //FIXME colors!!!!!!!!!!!!!!
        @SuppressLint("SimpleDateFormat")
        fun bind(customerLessonWithAgenda: CustomerLessonWithAgenda) {
            //start view
            itemBinding.weekDay.visibility = View.VISIBLE
            itemBinding.weekDay.paintFlags = itemBinding.weekDay.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemBinding.date.paintFlags = itemBinding.date.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemBinding.image.colorFilter = null
            itemBinding.icon.setImageDrawable(null)
            //itemBinding.date.setTextColor(Color.BLACK)

            customerLessonWithAgenda.date?.let {
                val weekPattern = SimpleDateFormat("E")
                val datePattern = SimpleDateFormat("dd.MM")

                itemBinding.date.text = datePattern.format(it)
                itemBinding.weekDay.text = weekPattern.format(it).capitalize()
            }
            //FIXME !!!!!!!!!!!!!!!!! fun
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.NONE){
                itemView.setBackgroundResource(R.drawable.layout_border_white)
                itemBinding.date.text = "Сейчас"
                //itemBinding.date.setTextColor(parseColor("#2E7D32"))
                itemBinding.weekDay.visibility = View.GONE
                itemBinding.image.setImageResource(R.drawable.ion_flag)
                itemBinding.image.setColorFilter(parseColor("#2E7D32"))
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_FREE){
                itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                itemBinding.image.setColorFilter(parseColor("#FFA000"))
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.CANCELED){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24)
                itemBinding.date.paintFlags = itemBinding.date.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemBinding.weekDay.paintFlags = itemBinding.weekDay.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.PLANNED ){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.FORGOT){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.question)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_NOT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_red)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                itemBinding.image.setColorFilter(parseColor("#C62828"))
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24)
                itemBinding.image.setColorFilter(parseColor("#C62828"))
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.PREPAID){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.image.setColorFilter(parseColor("#2E7D32"))
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.VISIT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
                itemBinding.image.setColorFilter(parseColor("#2E7D32"))
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.VISIT_NOT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_red)
                selectImageType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
                itemBinding.image.setColorFilter(parseColor("#C62828"))
            }
        }
    }

    private fun selectImageType(itemBinding: RecyclerViewRawBinding, status : String){
        when(status){
            "1" -> itemBinding.image.setImageResource(R.drawable.person_grey)
            "2" -> itemBinding.image.setImageResource(R.drawable.person_stalker)
            "3" -> itemBinding.image.setImageResource(R.drawable.asterisk)
            "4" -> itemBinding.image.setImageResource(R.drawable.paperplanenew)
            "5" -> itemBinding.image.setImageResource(R.drawable.md_contacts)
            "6" -> itemBinding.image.setImageResource(R.drawable.asterisk)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewRawBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rssItem = getItem(position)
        rssItem?.let { holder.bind(it) }
    }
}

class LessonDiffUtilCallback : DiffUtil.ItemCallback<CustomerLessonWithAgenda>() {
    override fun areItemsTheSame(oldItem: CustomerLessonWithAgenda, newItem: CustomerLessonWithAgenda): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CustomerLessonWithAgenda, newItem: CustomerLessonWithAgenda): Boolean {
        return oldItem.id == newItem.id
    }
}

class LessonRecyclerItemListener(val clickListener: (customerLessonWithAgenda: CustomerLessonWithAgenda) -> Unit) {
    fun onClick(customerLessonWithAgenda: CustomerLessonWithAgenda) = clickListener(customerLessonWithAgenda)
}