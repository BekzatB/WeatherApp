package kz.data.data_souce.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import kz.data.model.LocalWeatherData

const val LOCAL_WEATHER_DATA = "local_weather_data"

class LocalDataSourceImpl(private val sharedPreferences: SharedPreferences, private val parser: Gson) :
    LocalDataSource {

    override fun getLocalData(): LocalWeatherData? {
        val json = sharedPreferences.getString(LOCAL_WEATHER_DATA, null)

        return if (json.isNullOrEmpty()) {
            null
        } else {
            return parser.fromJson(json, LocalWeatherData::class.java)
        }
    }

    override fun saveLocalData(localWeatherData: LocalWeatherData) {
        sharedPreferences.edit {
            remove(LOCAL_WEATHER_DATA)
            putString(LOCAL_WEATHER_DATA, parser.toJson(localWeatherData))
        }
    }
}


interface LocalDataSource {
    fun getLocalData(): LocalWeatherData?
    fun saveLocalData(localWeatherData: LocalWeatherData)
}