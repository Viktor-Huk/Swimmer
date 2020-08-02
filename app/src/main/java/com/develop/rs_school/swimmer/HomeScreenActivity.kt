package com.develop.rs_school.swimmer

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.develop.rs_school.swimmer.network.getCustomerLessonsWithFullInfo
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeScreenActivity : BaseActivity(1){

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    private val adapter = LessonRecyclerAdapter(
        LessonRecyclerItemListener{}
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_layout)
        setupBottomNavigationBar()
        initRecyclerView()
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()


        uiScope.launch {
            adapter.submitList(getCustomerLessonsWithFullInfo("2376"))
        }
    }

    private fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this@HomeScreenActivity)
        recyclerview.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
