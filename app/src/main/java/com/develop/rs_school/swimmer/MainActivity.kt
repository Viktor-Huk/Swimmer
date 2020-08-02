package com.develop.rs_school.swimmer

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //val dataViewModel by viewModels<DataViewModel>()
    lateinit var dataViewModel: DataViewModel
    lateinit var viewModelFactory: DataViewModelFactory

    //FIXME sorry)
    var currentFragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customerId = getSavedSession()

        viewModelFactory = DataViewModelFactory(customerId)
        dataViewModel = ViewModelProvider(this, viewModelFactory).get(DataViewModel::class.java)

        bottom_navigation_view.setIconSize(29f, 29f)
            .setTextVisibility(false)
            .enableAnimation(true)
        for (i in 0 until bottom_navigation_view.menu.size()) {
            bottom_navigation_view.setIconTintList(i, null)
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile_button -> {
                    if (currentFragment == 2) return@setOnNavigationItemSelectedListener true
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fl_content, ProfileFragment())
                        .commit()
                    currentFragment = 2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.lessons_button -> {
                    if (currentFragment == 1) return@setOnNavigationItemSelectedListener true
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fl_content, LessonsFragment())
                        .commit()
                    currentFragment = 1
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    Log.e("1", "Unknown nav item clicked :  $it")
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_content, LessonsFragment())
                .commit()
            currentFragment = 1
        }
    }

    private fun getSavedSession(): String {
        val sharedPref = getSharedPreferences(getString(R.string.app_pref), Context.MODE_PRIVATE)
        return sharedPref.getString(getString(R.string.sessionId), "2376") ?: "2376"
    }

    //override fun onBackPressed() {}
}