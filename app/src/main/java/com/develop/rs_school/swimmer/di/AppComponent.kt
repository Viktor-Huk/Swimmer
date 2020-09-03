package com.develop.rs_school.swimmer.di
import android.content.Context
import com.develop.rs_school.swimmer.presentation.main.ui.LessonsFragment
import com.develop.rs_school.swimmer.presentation.main.ui.MainActivity
import com.develop.rs_school.swimmer.presentation.main.ui.ProfileFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Definition of a Dagger component
@Singleton
@Component(modules = [StorageModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: MainActivity)
    fun inject(fragment: LessonsFragment)
    fun inject(fragment: ProfileFragment)
}