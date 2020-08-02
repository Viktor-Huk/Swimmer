package com.develop.rs_school.swimmer

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.custom_layout.view.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    lateinit var button_logout: Button
    private var ctx: Context? = null
    private var self: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ctx = container?.context
        var self = inflater.inflate(R.layout.fragment_settings, container, false)
        val btn = self?.findViewById<Button>(R.id.button_logout)
        btn?.setOnClickListener {
            showMessageBox("Some msg")
        }
        return self
    }

    fun showMessageBox(text: String) {

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.custom_layout, null)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)

        //show dialog
        val messageBoxInstance = messageBoxBuilder.show()

        //set Listener
        messageBoxView.setOnClickListener() {
            //close dialog
            messageBoxInstance.dismiss()
        }
    }


}