package kz.weatherapp.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil
import kz.domain.entities.WeatherData

class WeatherDiffUtils(
    private val oldList: List<WeatherData>,
    private val newList: List<WeatherData>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}