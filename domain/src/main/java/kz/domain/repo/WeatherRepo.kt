package kz.domain.repo

import kz.domain.RemoteUseCaseResult
import kz.domain.entities.WeatherData

interface WeatherRepo {
    suspend fun getCityWeatherData(q: String): RemoteUseCaseResult<WeatherData>
}