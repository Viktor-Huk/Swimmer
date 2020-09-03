package com.develop.rs_school.swimmer.presentation.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.SwimmerApp
import com.develop.rs_school.swimmer.databinding.FragmentProfileBinding
import com.develop.rs_school.swimmer.presentation.login.LoginActivity
import com.develop.rs_school.swimmer.presentation.main.viewModels.MainViewModel
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.profile.observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.profileName.text = it.name
                binding.dob.text = it.dob
                binding.paidDays.text = it.paid_lesson.toString()
                binding.email.text = it.email
                binding.phone.text = it.phone
                binding.balance.text = it.balance
            }
        })

        //FIXME  vm.logout clicked -> in vm delete all and event go to login
        binding.logoutButton.setOnClickListener {
            mainViewModel.deleteSession()
            mainViewModel.deleteData()
            openActivity()
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