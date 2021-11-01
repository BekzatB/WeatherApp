package kz.weatherapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.data.data_souce.local.LocalDataSource
import kz.data.model.*
import kz.domain.entities.WeatherData
import kz.domain.usecase.GetCityWeatherUseCase
import kz.weatherapp.base.BaseViewModel
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val getCityWeatherUseCase: GetCityWeatherUseCase,
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    var weatherData = mutableListOf<WeatherData>()
    private var placesNumber: Int = 0
    private var errorNumber: Int = 0

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun dispatch(action: Action) {

        when (action) {
            is Action.Search -> search(query = action.query, searchPlacesNumber = action.searchPlacesNumber)
            is Action.SaveData -> saveData(action.weatherDataList, action.queryText)
            is Action.GetSavedData -> getSavedData()
        }
    }

    fun clearWeatherData() {
        weatherData.clear()
        placesNumber = 0
        errorNumber = 0
    }

    private fun search(query: String, searchPlacesNumber: Int) {
        _state.postValue(State.Loading)
        viewModelScope.launch {
            getCityWeatherUseCase.execute(query).doOnSuccess { result ->
                weatherData.add(result)
            }.doOnComplete {
                placesNumber += 1

                if (placesNumber == searchPlacesNumber && errorNumber != searchPlacesNumber) {
                    _state.postValue(State.Success(weatherData))

                }
            }.doOnError { error ->
                errorNumber += 1

                if (errorNumber == searchPlacesNumber) {
                    _state.postValue(State.Error(error))
                }
            }
        }
    }

    private fun saveData(weatherDataList: List<WeatherData>, query: String) {
        localDataSource.saveLocalData(
            weatherDataList.toLocalWeather(query)
        )
    }

    private fun getSavedData() {
        val savedData = localDataSource.getLocalData()
        _state.postValue(
            State.LocalSavedData(
                savedData?.weatherData?.toWeatherData() ?: emptyList(),
                savedData?.searchQuery ?: "",
                savedData?.time ?: System.currentTimeMillis()
            )
        )
    }
}

sealed class Action {
    data class Search(val query: String, val searchPlacesNumber: Int) : Action()
    data class SaveData(val weatherDataList: List<WeatherData>, val queryText: String) : Action()
    object GetSavedData : Action()
}

sealed class State {
    data class Success(val weatherDataList: List<WeatherData>) : State()
    data class Error(val error: String) : State()
    object Loading : State()
    data class LocalSavedData(val weatherDataList: List<WeatherData>, val query: String, val time: Long) : State()
}

private fun List<WeatherData>.toLocalWeather(query: String): LocalWeatherData {
    return LocalWeatherData(
        weatherData = this.map {
            WeatherDataLocal(
                main = MainLocal(
                    it.main.temp
                ),
                name = it.name,
                weather = it.weather.map { weather ->
                    WeatherLocal(
                        weather.description,
                        weather.icon,
                        weather.id,
                        weather.main
                    )
                }

            )
        },
        time = System.currentTimeMillis(),
        searchQuery = query
    )
}