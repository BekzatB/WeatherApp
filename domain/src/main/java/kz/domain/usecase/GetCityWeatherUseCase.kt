package kz.domain.usecase

import kz.domain.RemoteUseCaseResult
import kz.domain.entities.WeatherData

interface GetCityWeatherUseCase {
    suspend fun execute(q: String): RemoteUseCaseResult<WeatherData>
}