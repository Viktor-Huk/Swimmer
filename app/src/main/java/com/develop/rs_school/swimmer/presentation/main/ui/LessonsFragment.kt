package com.develop.rs_school.swimmer.presentation.main.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.develop.rs_school.swimmer.SwimmerApp
import com.develop.rs_school.swimmer.data.Result
import com.develop.rs_school.swimmer.databinding.FragmentLessonsBinding
import com.develop.rs_school.swimmer.presentation.main.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class LessonsFragment : Fragment() {

    private var _binding : FragmentLessonsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter =
        LessonRecyclerAdapter(
            LessonRecyclerItemListener {}
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as SwimmerApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lessonRecycler.adapter = adapter

        //val model = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.lessons.observe(viewLifecycleOwner, Observer {
            Log.d("1", "updating $it") //FIXME double updating on start problem LAZY ?
            it?.apply {
                val t = if(it is Result.Success) it.data else listOf()
                adapter.submitList(t)
            }
        })

        mainViewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if(it != null)
                binding.swipeRefresh.isRefreshing = it
        })

        //FIXME make hidden bar
        mainViewModel.showError.observe(viewLifecycleOwner, Observer {
            if(it != null)
                Snackbar.make(
                    binding.root,
                    "Network error, local data displayed",
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