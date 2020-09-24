package com.develop.rs_school.swimmer.di

import com.develop.rs_school.swimmer.presentation.main.ui.MainActivity
import dagger.Subcomponent

// Definition of a Dagger subcomponent
// @ActivityScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
}