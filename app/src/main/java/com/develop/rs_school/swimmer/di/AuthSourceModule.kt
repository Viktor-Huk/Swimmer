package com.develop.rs_school.swimmer.di

import com.develop.rs_school.swimmer.data.AuthSource
import com.develop.rs_school.swimmer.data.NetworkAuthSource
import com.develop.rs_school.swimmer.data.NetworkWithoutSmsAuthSource
import dagger.Binds
import dagger.Module
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
abstract class AuthSourceModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NetworkAuth

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NetworkWithoutSms

    @Singleton
    @NetworkAuth
    @Binds
    abstract fun provideNetworkAuthSource(networkAuthSource: NetworkAuthSource): AuthSource

    @Singleton
    @NetworkWithoutSms
    @Binds
    abstract fun provideNetworkWithoutSmsSource(networkWithoutSmsAuthSource: NetworkWithoutSmsAuthSource): AuthSource
}