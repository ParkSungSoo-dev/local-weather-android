package dev.soaptree.local.weather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.soaptree.local.weather.Weather
import dev.soaptree.local.weather.databinding.ItemWeatherListBinding

class WeatherListAdapter : RecyclerView.Adapter<WeatherListAdapter.WeatherListViewHolder>() {
    var weathers: ArrayList<Weather> = ArrayList()
    set(value) {
        weathers.clear()
        weathers.addAll(value)
        notifyDataSetChanged()
    }
    class WeatherListViewHolder(private var itemWeatherListBinding: ItemWeatherListBinding) :
            RecyclerView.ViewHolder (itemWeatherListBinding.root) {
        fun bind(weather: Weather) {
            itemWeatherListBinding.weather = weather
            itemWeatherListBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        return WeatherListViewHolder(ItemWeatherListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return weathers.size
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
        val weather = weathers[position]
        holder.bind(weather)
    }
}