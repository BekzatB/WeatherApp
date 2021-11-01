package kz.weatherapp.di

import kz.weatherapp.base.BaseResourcesManager
import kz.weatherapp.base.ResourcesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val baseModule = module {
    single<BaseResourcesManager> { ResourcesManager(androidContext()) }
}
