package com.develop.rs_school.swimmer

import android.os.Bundle
import android.widget.Toast

class AccoutActivity : BaseActivity(1){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()
    }
}
