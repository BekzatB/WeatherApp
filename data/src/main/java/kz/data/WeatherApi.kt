package kz.data

import kz.data.api.NetResult
import kz.data.model.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("weather")
    suspend fun getCityWeatherData(
        @Query("q") q: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = "aee152f7fe7a9a219cbdcbb3ba24c279"
    ): NetResult<WeatherDataResponse>


}