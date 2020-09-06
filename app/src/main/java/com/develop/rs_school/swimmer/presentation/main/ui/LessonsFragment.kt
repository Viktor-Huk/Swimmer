package com.develop.rs_school.swimmer.presentation.main.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.databinding.FragmentLessonsBinding
import com.google.android.material.snackbar.Snackbar

class LessonsFragment : Fragment() {

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter =
        LessonRecyclerAdapter(
            LessonRecyclerItemListener {}
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lessonRecycler.adapter = adapter

        val mainViewModel = (requireActivity() as MainActivity).mainViewModel

        mainViewModel.lessons.observe(viewLifecycleOwner, Observer {
            Log.d("1", "updating $it")
            it?.apply {
                adapter.submitList(it)
            }
        })

        mainViewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if (it != null)
                binding.swipeRefresh.isRefreshing = it
        })

        mainViewModel.showError.observe(viewLifecycleOwner, Observer {
            if (it != null)
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_network_message),
                    Snackbar.LENGTH_SHORT
                ).show()
        })

        binding.swipeRefresh.setOnRefreshListener { mainViewModel.updateData() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}