package com.develop.rs_school.swimmer.di

import com.develop.rs_school.swimmer.presentation.main.ui.LessonsFragment
import com.develop.rs_school.swimmer.presentation.main.ui.MainActivity
import com.develop.rs_school.swimmer.presentation.main.ui.ProfileFragment
import dagger.Subcomponent

// Definition of a Dagger subcomponent
//@ActivityScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: LessonsFragment)
    fun inject(fragment: ProfileFragment)
}
