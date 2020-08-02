package com.develop.rs_school.swimmer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(requireActivity()).get(DataViewModel::class.java)
        model.profile.observe(viewLifecycleOwner, Observer {
            tv_name.text = it.name
            dob.text = it.dob
            val paidStatus = it.paid_lesson.toString()
            if(paidStatus.toInt() < 0){
                paied_visits.text = "You need to pay for ${it.paid_lesson.toString()} days"
            }else {
                paied_visits.text = it.paid_lesson.toString()
            }
            email_f.text = it.email.firstOrNull()
            phone_f.text = it.phone.firstOrNull()
            payment_f.text = it.paidDate.toString()

        })

        view.logout_button.setOnClickListener {
            deleteSession()
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

    override fun onDestroy() {
        super.onDestroy()
    }


}