package kz.weatherapp.di

import kz.weatherapp.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel {
        MainViewModel(
            get(),
            get()
        )
    }
}