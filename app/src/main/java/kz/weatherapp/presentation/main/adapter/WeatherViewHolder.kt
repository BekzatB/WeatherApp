package kz.weatherapp.presentation.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kz.domain.entities.WeatherData
import kz.weatherapp.base.BaseAdapter
import kz.weatherapp.databinding.ListItemSearchedCityTemperatureBinding

class WeatherViewHolder(parent: ViewGroup) :
    BaseAdapter.ViewHolder<WeatherData, ListItemSearchedCityTemperatureBinding>(
        ListItemSearchedCityTemperatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ) {

    override fun show(data: WeatherData) {
        binding.textCityName.text = data.name
        binding.textTemperature.text = data.main.temp.toString()
    }
}