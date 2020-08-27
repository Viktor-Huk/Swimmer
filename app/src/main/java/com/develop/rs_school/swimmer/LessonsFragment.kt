package com.develop.rs_school.swimmer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.develop.rs_school.swimmer.databinding.FragmentLessonsBinding

class LessonsFragment : Fragment() {

    private var _binding : FragmentLessonsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter = LessonRecyclerAdapter(
        LessonRecyclerItemListener{}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lessonRecycler.adapter = adapter
        binding.swipeRefresh.isRefreshing = true

        val model = ViewModelProvider(requireActivity()).get(DataViewModel::class.java)
        model.lessons.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing = false
        })

        binding.swipeRefresh.setOnRefreshListener { model.updateData() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}