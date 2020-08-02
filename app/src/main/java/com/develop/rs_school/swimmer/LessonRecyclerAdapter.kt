package com.develop.rs_school.swimmer


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.develop.rs_school.swimmer.databinding.RecyclerViewRawBinding
import com.develop.rs_school.swimmer.model.AgendaStatus
import com.develop.rs_school.swimmer.model.CustomerLessonWithAgenda

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

            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.MISSED_FREE){
                //TODO ...
                itemView.setBackgroundColor(Color.GREEN)
                itemBinding.image.setImageResource(R.drawable.person_icon)
            }
            if(customerLessonWithAgenda.agendaStatus == AgendaStatus.CANCELED){
                //TODO ...
            }
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