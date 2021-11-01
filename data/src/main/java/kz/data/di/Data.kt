package kz.data.di

import android.content.Context
import kz.data.data_souce.local.LOCAL_WEATHER_DATA
import kz.data.data_souce.local.LocalDataSource
import kz.data.data_souce.local.LocalDataSourceImpl
import kz.data.data_souce.remote.RemoteDataSource
import kz.data.data_souce.remote.RemoteDataSourceImpl
import kz.data.repository.WeatherRepository
import kz.domain.repo.WeatherRepo
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    single<RemoteDataSource> {
        RemoteDataSourceImpl(get())
    }

    single<WeatherRepo> {
        WeatherRepository(
            get()
        )
    }

    single() {
        androidContext().getSharedPreferences(LOCAL_WEATHER_DATA, Context.MODE_PRIVATE)
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(get(), get())
    }


}
