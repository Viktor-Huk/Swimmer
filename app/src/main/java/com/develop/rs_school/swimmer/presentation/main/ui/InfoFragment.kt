package com.develop.rs_school.swimmer.presentation.main.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.develop.rs_school.swimmer.databinding.InfoFragmentBinding
import com.develop.rs_school.swimmer.presentation.main.viewModels.InfoViewModel

class InfoFragment : Fragment() {

    private lateinit var viewModel: InfoViewModel

    private var _binding: InfoFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)

        binding.social.movementMethod = LinkMovementMethod.getInstance()
        binding.youtubeChannel.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}