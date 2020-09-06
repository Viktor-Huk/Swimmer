package com.develop.rs_school.swimmer.di

import com.develop.rs_school.swimmer.data.AuthSource
import com.develop.rs_school.swimmer.data.NetworkAuthSource
import com.develop.rs_school.swimmer.data.NetworkWithoutSmsAuthSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
object AuthSourceModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NetworkAuth

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NetworkWithoutSms


    @Singleton
    @NetworkAuth
    @Provides
    fun provideNetworkAuthSource(): AuthSource = NetworkAuthSource()

    @Singleton
    @NetworkWithoutSms
    @Provides
    fun provideNetworkWithoutSmsSource(): AuthSource = NetworkWithoutSmsAuthSource()
}