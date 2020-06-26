package dev.soaptree.local.weather.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.soaptree.local.weather.Location
import dev.soaptree.local.weather.Weather
import dev.soaptree.local.weather.repository.WeatherApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationWeatherViewModel : ViewModel() {
    private val _weathers = MutableLiveData<ArrayList<Weather>>()
    private val locations = MutableLiveData<List<Location>>()
    private val weatherApiService = WeatherApiService.create()

    private val _isRequesting = MutableLiveData<Boolean>()

    val isRequesting: LiveData<Boolean> = _isRequesting
    val weathers: LiveData<ArrayList<Weather>> = _weathers

    // TODO: change to coroutine
    fun searchLocationWeathers(location: String) {
        _isRequesting.value = true
        weatherApiService.requestLocationSearch(location).enqueue(object : Callback<List<Location>> {
            override fun onResponse(
                call: Call<List<Location>>,
                response: Response<List<Location>>
            ) {
                if (response.isSuccessful) {
                    _weathers.value = null
                    response.body()?.let {
                        val newLocationWeathers = ArrayList<Weather>()
                        locations.value = it
                        var count = 0
                        it.forEach { location ->
                            weatherApiService.requestLocationWeather(location.whereOnEarthId).enqueue(object : Callback<Weather> {
                                override fun onResponse(
                                    call: Call<Weather>,
                                    response: Response<Weather>
                                ) {
                                    if (response.isSuccessful) {
                                        Log.d("test_code", "response.body: " + response.body())
                                        response.body()?.let { newLocationWeather ->
                                            newLocationWeathers.add(newLocationWeather)
                                        }
                                    }
                                    count++
                                    if (count == it.size) {
                                        _weathers.value = newLocationWeathers
                                        _isRequesting.value = false
                                    }
                                }

                                override fun onFailure(call: Call<Weather>, t: Throwable) {
                                    TODO("Not yet implemented")
                                    count++
                                    if (count == it.size) {
                                        _weathers.value = newLocationWeathers
                                        _isRequesting.value = false
                                    }
                                }
                            })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}