package com.develop.rs_school.swimmer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_lessons.*

class LessonsFragment : Fragment() {

    private val adapter = LessonRecyclerAdapter(
        LessonRecyclerItemListener{}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lessons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lesson_recycler.adapter = adapter

        val model = ViewModelProvider(requireActivity()).get(DataViewModel::class.java)
        model.lessons.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}