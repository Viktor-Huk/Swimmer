package com.develop.rs_school.swimmer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast


class AccoutActivity : BaseActivity(0){

    lateinit var settingsButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
//        setupBottomNavigationBar()
//        setContentView(R.layout.toolbar_layout)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_layout)


        Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()

        settingsButton = findViewById(R.id.settings) as ImageButton


        settingsButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
