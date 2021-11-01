package kz.weatherapp.presentation.main.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import kz.domain.entities.WeatherData
import kz.weatherapp.base.BaseAdapter

class WeatherAdapter : BaseAdapter<WeatherData, ViewBinding>() {

    override fun setItems(items: List<WeatherData>) {
        val diffCallback = WeatherDiffUtils(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(items)

        diffResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<WeatherData, ViewBinding> {
        return WeatherViewHolder(parent) as ViewHolder<WeatherData, ViewBinding>
    }
}