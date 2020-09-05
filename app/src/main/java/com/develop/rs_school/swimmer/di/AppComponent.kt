package com.develop.rs_school.swimmer.di

import android.content.Context
import com.develop.rs_school.swimmer.presentation.login.LoginActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Definition of a Dagger component
@Singleton
@Component(modules = [DataSourceModule::class, SessionSourceModule::class, AppSubcomponents::class, ViewModelBuilderModule::class, LoginModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: LoginActivity)

    fun mainActivityComponent(): MainComponent.Factory
}