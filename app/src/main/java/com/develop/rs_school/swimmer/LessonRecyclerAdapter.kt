package com.develop.rs_school.swimmer


import android.view.LayoutInflater
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
                getItem(adapterPosition)?.let { itemClickListener.onClick(it) }
            }
        }

        fun bind(customerLessonWithAgenda: CustomerLessonWithAgenda) {
//            Glide.with(itemView.context).load(rssItem.imageUrl)
//                .thumbnail(GLIDE_THUMBNAIL_SIZE)
//                .into(itemBinding.rssImage)
//            itemBinding. .duration.text = rssItem.duration
//            itemBinding.title.text = rssItem.title
//            itemBinding.speaker.text = rssItem.speaker
            //itemBinding.name.text = customerLessonWithAgenda.date.toString()

            itemBinding.date.text = customerLessonWithAgenda.date?.toString()
            itemBinding.weekDay.text = customerLessonWithAgenda.date.toString().split("\\s".toRegex())[0]
            val pattern = "dd.MM"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date: String = simpleDateFormat.format(customerLessonWithAgenda.date)
            itemBinding.date.text = date


            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_FREE){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.CANCELED){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.PLANNED ){
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.FORGOT){
                itemBinding.icon.setImageResource(R.drawable.question)
                itemView.setBackgroundResource(R.drawable.layout_border_gray)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_NOT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_red)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.PREPAID){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.VISIT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_green)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.VISIT_NOT_PAID){
                itemView.setBackgroundResource(R.drawable.layout_border_red)
                itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                selectIconType(itemBinding, customerLessonWithAgenda.type)
            }
        }
    }

    private fun selectIconType(itemBinding: RecyclerViewRawBinding, status : String){
        when(status){
            "1" -> itemBinding.image.setImageResource(R.drawable.person_icon)
            "2" -> itemBinding.image.setImageResource(R.drawable.person_stalker)
            "3" -> itemBinding.image.setImageResource(R.drawable.asterisk)
            "4" -> itemBinding.image.setImageResource(R.drawable.paperplane)
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