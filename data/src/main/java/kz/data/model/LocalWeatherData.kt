package kz.data.model

import kz.domain.entities.WeatherData
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalWeatherData(
    val weatherData: List<WeatherDataLocal>,
    val time: Long,
    val searchQuery: String
) : Parcelable

@Parcelize
data class WeatherDataLocal(
    val main: MainLocal,
    val name: String,
    val weather: List<WeatherLocal>
) : Parcelable {
}

@Parcelize
data class WeatherLocal(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Parcelable

@Parcelize
data class MainLocal(
    val temp: Double
) : Parcelable

fun List<WeatherDataLocal>.toWeatherData(): List<WeatherData> {
    return this.map {
        WeatherData(
            main = WeatherData.Main(
                it.main.temp
            ),
            name = it.name,
            weather = it.weather.map {
                WeatherData.Weather(
                    description = it.description,
                    icon = it.icon,
                    id = it.id,
                    main = it.main
                )
            }
        )
    }
}