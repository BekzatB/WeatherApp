package kz.data.repository

import kz.data.data_souce.local.LocalDataSource
import kz.data.data_souce.remote.RemoteDataSource
import kz.domain.RemoteUseCaseResult
import kz.domain.entities.WeatherData
import kz.domain.repo.WeatherRepo

class WeatherRepository(private val remoteDataSource: RemoteDataSource) : WeatherRepo {

    override suspend fun getCityWeatherData(q: String): RemoteUseCaseResult<WeatherData> {
        return remoteDataSource.getCityWeatherData(q)
    }
}