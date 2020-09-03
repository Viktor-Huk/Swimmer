package com.develop.rs_school.swimmer

import android.app.Application
import com.develop.rs_school.swimmer.di.AppComponent
import com.develop.rs_school.swimmer.di.DaggerAppComponent

open class SwimmerApp : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }
}