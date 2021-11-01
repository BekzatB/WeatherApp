package kz.data.data_souce.remote

import kz.data.WeatherApi
import kz.data.api.toRemoteUseCaseResult
import kz.domain.RemoteUseCaseResult
import kz.domain.entities.WeatherData

class RemoteDataSourceImpl(private val api: WeatherApi) : RemoteDataSource {
    override suspend fun getCityWeatherData(q: String): RemoteUseCaseResult<WeatherData> {
        return api.getCityWeatherData(q).toRemoteUseCaseResult()
    }
}

interface RemoteDataSource {
    suspend fun getCityWeatherData(q: String): RemoteUseCaseResult<WeatherData>
}