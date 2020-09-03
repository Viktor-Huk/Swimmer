package com.develop.rs_school.swimmer.di

import androidx.lifecycle.ViewModel
import com.develop.rs_school.swimmer.presentation.main.viewModels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}