package com.develop.rs_school.swimmer

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.Toast


class AccoutActivity : BaseActivity(0){

    lateinit var back: ImageButton
    lateinit var settingsButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        setContentView(R.layout.account_layout)
        Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()

        back = findViewById<View>(R.id.backToMain) as ImageButton
        settingsButton = findViewById(R.id.settings) as ImageButton

        back.setOnClickListener {
            it.startAnimation(AlphaAnimation(1f, 0.8f))
            onBackPressed()
        }

        settingsButton.setOnClickListener {
            it.startAnimation(AlphaAnimation(1f, 0.8f))
            supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment())
                .commit()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
