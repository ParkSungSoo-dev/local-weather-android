package dev.soaptree.local.weather.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.soaptree.local.weather.R
import dev.soaptree.local.weather.Weather
import dev.soaptree.local.weather.databinding.ItemWeatherListBinding

class WeatherListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_HOLDER_TYPE_HEADER = 0
    private val VIEW_HOLDER_TYPE_ITEM = 1

    private val HEADER_COUNT = 1

    var weathers: ArrayList<Weather> = ArrayList()
    set(value) {
        weathers.clear()
        weathers.addAll(value)
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
        var weathersCount = weathers.size
        if (weathersCount != 0) {
            weathersCount += HEADER_COUNT
        }
        return weathersCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WeatherListItemViewHolder -> {
                val weather = weathers[position - HEADER_COUNT]
                holder.bind(weather)
            }
        }
    }

    class WeatherListHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class WeatherListItemViewHolder(private var itemWeatherListBinding: ItemWeatherListBinding) :
        RecyclerView.ViewHolder (itemWeatherListBinding.root) {
        fun bind(weather: Weather) {
            itemWeatherListBinding.weather = weather
            itemWeatherListBinding.executePendingBindings()
        }
    }

}