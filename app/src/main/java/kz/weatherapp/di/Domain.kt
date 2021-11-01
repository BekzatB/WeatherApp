package kz.weatherapp.di

import kz.domain.interactors.GetCityWeatherInteractor
import kz.domain.usecase.GetCityWeatherUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetCityWeatherUseCase> {
        GetCityWeatherInteractor(get())
    }
}