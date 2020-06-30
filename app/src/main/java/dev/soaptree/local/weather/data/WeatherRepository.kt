package dev.soaptree.local.weather.data

import dev.soaptree.local.weather.data.remote.WeatherApiService
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.RuntimeException

object WeatherRepository {
    private val weatherApiService = WeatherApiService.create()

    suspend fun getLocationSearchedList(coroutineScope: CoroutineScope, location: String) : List<LocationSearched>? {
        var newLocationSearchedList: List<LocationSearched>? = null
        coroutineScope.launch(Dispatchers.IO) {
            val locationsResponse = weatherApiService.getLocationSearchedList(location)
            if (!locationsResponse.isSuccessful) {
                throw RuntimeException("Failed to request locations. code: ${locationsResponse.code()}, message: ${locationsResponse.message()}")
            }
            newLocationSearchedList = locationsResponse.body()
        }.join()
        return newLocationSearchedList
    }

    suspend fun getLocationWeathers(coroutineScope: CoroutineScope, locationSearchedList: List<LocationSearched>?) : ArrayList<LocationWeather> {
        val newLocationWeathers: ArrayList<LocationWeather> = ArrayList()
        val locationWeatherDeferredResponseList = ArrayList<Deferred<Response<LocationWeather>>>()
        coroutineScope.launch {
            locationSearchedList?.forEach { location ->
                val weatherDeferred = coroutineScope.async(Dispatchers.IO) {
                    weatherApiService.getLocationWeather(location.whereOnEarthId)
                }
                locationWeatherDeferredResponseList.add(weatherDeferred)
            }
            locationWeatherDeferredResponseList.forEach{ weatherDeferredResponse ->
                val weatherResponse = weatherDeferredResponse.await()
                if (!weatherResponse.isSuccessful) {
                    throw RuntimeException("Failed to getLocationWeather. code: ${weatherResponse.code()}, message: ${weatherResponse.message()}")
                }
                weatherResponse.body()?.let { newLocationWeathers.add(it) }
            }
        }.join()
        return newLocationWeathers
    }
}