package com.develop.rs_school.swimmer

import android.os.Bundle
import android.widget.Toast

class HomeScreenActivity : BaseActivity(0){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
    }
}
