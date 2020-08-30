package com.develop.rs_school.swimmer.presentation.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.databinding.FragmentProfileBinding
import com.develop.rs_school.swimmer.presentation.login.LoginActivity
import com.develop.rs_school.swimmer.presentation.main.viewModels.DataViewModel

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(requireActivity()).get(DataViewModel::class.java)
        model.profile.observe(viewLifecycleOwner, Observer {
            binding.tvName.text = it.name
            binding.dob.text = it.dob
            binding.paiedVisits.text = it.paid_lesson.toString()
            binding.emailF.text = it.email
            binding.phoneF.text = it.phone
            binding.balanceF.text = it.balance
        })

        binding.logoutButton.setOnClickListener {
            deleteSession()
            model.deleteData()
            openActivity()
        }
    }

    private fun deleteSession(){
        val sharedPref = requireActivity().getSharedPreferences(getString(R.string.app_pref), Context.MODE_PRIVATE)
        sharedPref?.let{
            with (sharedPref.edit()) {
                remove(getString(R.string.sessionId))
                commit()
            }
        }
    }
    private fun openActivity() {
        startActivity(
            Intent(
                context,
                LoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}