package com.develop.rs_school.swimmer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //val dataViewModel by viewModels<DataViewModel>{DataViewModelFactory("t")}
    lateinit var dataViewModel: DataViewModel
    lateinit var viewModelFactory: DataViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModelFactory = DataViewModelFactory("2376")
        dataViewModel = ViewModelProvider(this, viewModelFactory).get(DataViewModel::class.java)

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

    //override fun onBackPressed() {}
}