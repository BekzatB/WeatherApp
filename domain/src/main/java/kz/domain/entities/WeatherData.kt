package kz.domain.entities

data class WeatherData(
    val main: Main,
    val name: String,
    val weather: List<Weather>,
)  {

    data class Main(
        val temp: Double,
    )

    data class Weather(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    )

}
