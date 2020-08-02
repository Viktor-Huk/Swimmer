package com.develop.rs_school.swimmer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.Toast


class AccoutActivity : BaseActivity(0){

    lateinit var settingsButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        setContentView(R.layout.account_layout)
        Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()

        settingsButton = findViewById(R.id.settings) as ImageButton


        settingsButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
