package com.develop.rs_school.swimmer.presentation.main.ui


import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.databinding.RecyclerViewRawBinding
import com.develop.rs_school.swimmer.AgendaStatus
import com.develop.rs_school.swimmer.domain.Lesson
import java.text.SimpleDateFormat
import java.util.*

class LessonRecyclerAdapter(private val itemClickListener: LessonRecyclerItemListener) :
    ListAdapter<Lesson, LessonRecyclerAdapter.ViewHolder>(
        LessonDiffUtilCallback()
    ) {

    inner class ViewHolder(private val itemBinding: RecyclerViewRawBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                //getItem(adapterPosition)?.let { itemClickListener.onClick(it) }
            }
        }

        //FIXME colors!!!!!!!!!!!!!!
        @SuppressLint("SimpleDateFormat")
        fun bind(lesson: Lesson) {
            //start view
            itemBinding.weekDay.visibility = View.VISIBLE
            itemBinding.weekDay.paintFlags = itemBinding.weekDay.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemBinding.date.paintFlags = itemBinding.date.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemBinding.image.colorFilter = null
            itemBinding.icon.setImageDrawable(null)
            //itemBinding.date.setTextColor(Color.BLACK)

            lesson.date?.let {
                val weekPattern = SimpleDateFormat("E", Locale("ru"))
                val datePattern = SimpleDateFormat("dd.MM", Locale("ru"))

                itemBinding.date.text = datePattern.format(it)
                itemBinding.weekDay.text = weekPattern.format(it).capitalize()
            }
            //FIXME !!!!!!!!!!!!!!!!! fun
            if(lesson.agendaStatus == AgendaStatus.NONE){
                itemView.setBackgroundResource(R.drawable.layout_border_white)
                itemBinding.date.text = "Сейчас"
                itemBinding.weekDay.visibility = View.GONE
                itemBinding.image.setImageResource(R.drawable.ion_flag)
                itemBinding.image.setColorFilter(parseColor("#2E7D32"))
            }
            if(lesson.agendaStatus == AgendaStatus.MISSED_FREE){
                itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                selectImageType(itemBinding, lesson.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                itemBinding.image.setColorFilter(parseColor("#FFA000"))
            }
            if(lesson.agendaStatus == AgendaStatus.CANCELED){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectImageType(itemBinding, lesson.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24)
                itemBinding.date.paintFlags = itemBinding.date.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemBinding.weekDay.paintFlags = itemBinding.weekDay.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            if(lesson.agendaStatus == AgendaStatus.PLANNED ){
                itemBinding.image.setColorFilter(parseColor("#575757"))
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectImageType(itemBinding, lesson.type)
            }
            if(lesson.agendaStatus == AgendaStatus.FORGOT){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectImageType(itemBinding, lesson.type)
                itemBinding.icon.setImageResource(R.drawable.question)
            }
            if(lesson.agendaStatus == AgendaStatus.MISSED_NOT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_red)
                selectImageType(itemBinding, lesson.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                itemBinding.image.setColorFilter(parseColor("#C62828"))
            }
            if(lesson.agendaStatus == AgendaStatus.MISSED_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                selectImageType(itemBinding, lesson.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                itemBinding.image.setColorFilter(parseColor("#C62828"))
            }
            if(lesson.agendaStatus == AgendaStatus.PREPAID){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                selectImageType(itemBinding, lesson.type)
                itemBinding.image.setColorFilter(parseColor("#2E7D32"))
            }
            if(lesson.agendaStatus == AgendaStatus.VISIT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                selectImageType(itemBinding, lesson.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
                itemBinding.image.setColorFilter(parseColor("#2E7D32"))
            }
            if(lesson.agendaStatus == AgendaStatus.VISIT_NOT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_red)
                selectImageType(itemBinding, lesson.type)
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

class LessonDiffUtilCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem.id == newItem.id
    }
}

class LessonRecyclerItemListener(val clickListener: (lesson: Lesson) -> Unit) {
    fun onClick(lesson: Lesson) = clickListener(lesson)
}