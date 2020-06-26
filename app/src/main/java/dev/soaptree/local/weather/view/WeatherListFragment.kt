package dev.soaptree.local.weather.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.soaptree.local.weather.R
import dev.soaptree.local.weather.Weather
import dev.soaptree.local.weather.databinding.FragmentWeatherListBinding
import kotlinx.android.synthetic.main.fragment_weather_list.*

class WeatherListFragment : Fragment() {
    private val locationWeatherViewModel  by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(
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

    companion object {
        @JvmStatic
        @BindingAdapter("listData")
        fun bindRecyclerView(recyclerView: RecyclerView, data: ArrayList<Weather>?) {
            Log.d("test_code", "bindRecyclerView, $data")
            data?.let {
                val adapter = recyclerView.adapter as WeatherListAdapter
                adapter.weathers = it
            }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listview_location_weather.adapter = WeatherListAdapter()
        listview_location_weather.setHasFixedSize(true)
        listview_location_weather.layoutManager = LinearLayoutManager(requireActivity()) // GridLayoutManager(requireActivity(), 1)

        swiperefreshlayout_location_weather.setOnRefreshListener {
            Log.d("test_code", "swiperefreshlayout_location_weather, onRefresh")
            swiperefreshlayout_location_weather.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        locationWeatherViewModel.searchLocationWeathers("se")
    }
}