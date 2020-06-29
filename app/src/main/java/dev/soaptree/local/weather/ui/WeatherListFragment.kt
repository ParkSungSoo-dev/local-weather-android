package dev.soaptree.local.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import dev.soaptree.local.weather.R
import dev.soaptree.local.weather.data.Weather
import dev.soaptree.local.weather.databinding.FragmentWeatherListBinding
import kotlinx.android.synthetic.main.fragment_weather_list.*

class WeatherListFragment : Fragment() {

    companion object {
        @JvmStatic
        @BindingAdapter("listData")
        fun bindRecyclerView(recyclerView: RecyclerView, data: ArrayList<Weather>?) {
            data?.let {
                val adapter = recyclerView.adapter
                if (adapter is WeatherListAdapter) {
                    adapter.weathers = it
                }
            }
        }
    }

    private val locationWeatherViewModel  by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
            LocationWeatherViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentWeatherListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_list, container, false)
        binding.lifecycleOwner = this
        binding.locationWeatherViewModel = locationWeatherViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listview_location_weather.apply {
            adapter = WeatherListAdapter()
            setHasFixedSize(true)
        }

        swiperefreshlayout_location_weather.setOnRefreshListener {
            locationWeatherViewModel.refreshWeathers { swiperefreshlayout_location_weather.isRefreshing = false }
        }
    }

    override fun onResume() {
        super.onResume()
        locationWeatherViewModel.searchLocationWeathers("se")
    }
}