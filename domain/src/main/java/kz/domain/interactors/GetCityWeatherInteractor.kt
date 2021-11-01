package kz.domain.interactors

import kz.domain.RemoteUseCaseResult
import kz.domain.entities.WeatherData
import kz.domain.repo.WeatherRepo
import kz.domain.usecase.GetCityWeatherUseCase

class GetCityWeatherInteractor(private val repo: WeatherRepo) : GetCityWeatherUseCase {
    override suspend fun execute(q: String): RemoteUseCaseResult<WeatherData> {
        return repo.getCityWeatherData(q)
    }
}