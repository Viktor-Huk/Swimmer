package com.develop.rs_school.swimmer.di

import com.develop.rs_school.swimmer.data.SessionSource
import com.develop.rs_school.swimmer.data.SharedPrefSessionSource
import dagger.Binds
import dagger.Module

@Module
abstract class SessionSourceModule {
    @Binds
    abstract fun provideSource(storage: SharedPrefSessionSource): SessionSource
}