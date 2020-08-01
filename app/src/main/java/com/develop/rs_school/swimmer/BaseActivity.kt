package com.develop.rs_school.swimmer

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_layout.*

abstract class BaseActivity(val navPos : Int) : AppCompatActivity() {

    private val TAG = "BaseActivity"

    fun setupBottomNavigationBar(){
        bottom_navigation_view.setIconSize(29f, 29f)
            .setTextVisibility(false)
            .enableAnimation(true)
        for(i in 0 until bottom_navigation_view.menu.size()){
            bottom_navigation_view.setIconTintList(i, null)
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            val nextActivity =
                when (it.itemId) {
                    R.id.home_button -> HomeScreenActivity::class.java
                    R.id.account_button -> AccoutActivity::class.java
                    else -> {
                        Log.e(TAG, "Unknown nav item clicked :  $it")
                        null
                    }
                }
            if (nextActivity != null) {
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0, 0)
                true
            } else {
                false
            }
        }
        bottom_navigation_view.menu.getItem(navPos).isChecked = true
    }
}