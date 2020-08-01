package com.develop.rs_school.swimmer

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast

class AccoutActivity : BaseActivity(1){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        setContentView(R.layout.account_layout)
        Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()
    }

}
