package com.develop.rs_school.swimmer

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val dataViewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation_view.setIconSize(29f, 29f)
            .setTextVisibility(false)
            .enableAnimation(true)
        for(i in 0 until bottom_navigation_view.menu.size()){
            bottom_navigation_view.setIconTintList(i, null)
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_content, ProfileFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.lessons_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_content, LessonsFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    Log.e("1", "Unknown nav item clicked :  $it")
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
        if(savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_content, LessonsFragment())
                .commit()
    }
}