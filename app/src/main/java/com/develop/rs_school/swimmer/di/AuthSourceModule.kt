package com.develop.rs_school.swimmer.di

import com.develop.rs_school.swimmer.data.AuthSource
import com.develop.rs_school.swimmer.data.NetworkAuthSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class AuthSourceModule {
    @Singleton
    @Binds
    abstract fun provideSource(storage: NetworkAuthSource): AuthSource
}