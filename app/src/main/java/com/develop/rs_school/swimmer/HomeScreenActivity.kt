package com.develop.rs_school.swimmer

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.toolbar_layout.*

class HomeScreenActivity : BaseActivity(1){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        initRecyclerView()
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
    }

    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    private fun initRecyclerView() {
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HomeScreenActivity)
            recyclerViewAdapter = RecyclerViewAdapter()
            adapter = recyclerViewAdapter
        }
    }
}
