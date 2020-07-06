package dev.soaptree.local.weather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.soaptree.local.weather.R
import dev.soaptree.local.weather.data.LocationWeather
import dev.soaptree.local.weather.databinding.ItemWeatherListBinding

class WeatherListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var locationWeathers: ArrayList<LocationWeather> = ArrayList()
        set(value) {
            locationWeathers.clear()
            locationWeathers.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_HOLDER_TYPE_HEADER -> WeatherListHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.header_weather_list, parent, false)
            )
            else -> WeatherListItemViewHolder(ItemWeatherListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_HOLDER_TYPE_HEADER
        } else {
            VIEW_HOLDER_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        var weathersCount = locationWeathers.size
        if (weathersCount != 0) {
            weathersCount += HEADER_COUNT
        }
        return weathersCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WeatherListItemViewHolder -> {
                val locationWeather = locationWeathers[position - HEADER_COUNT]
                holder.bind(locationWeather)
            }
        }
    }

    class WeatherListHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class WeatherListItemViewHolder(private var itemWeatherListBinding: ItemWeatherListBinding) :
        RecyclerView.ViewHolder (itemWeatherListBinding.root) {
        fun bind(currentLocationWeather: LocationWeather) {
            itemWeatherListBinding.run {
                locationWeather = currentLocationWeather
                executePendingBindings()
            }
        }
    }

    companion object {
        private const val VIEW_HOLDER_TYPE_HEADER = 0
        private const val VIEW_HOLDER_TYPE_ITEM = 1

        private const val HEADER_COUNT: Int = 1
    }
}