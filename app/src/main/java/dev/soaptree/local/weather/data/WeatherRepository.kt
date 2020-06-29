package dev.soaptree.local.weather.data

import dev.soaptree.local.weather.data.remote.WeatherApiService
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.RuntimeException

object WeatherRepository {
    private val weatherApiService = WeatherApiService.create()

    suspend fun getWeathers(coroutineScope: CoroutineScope, locations: List<Location>?) : ArrayList<Weather> {
        val newWeathers: ArrayList<Weather> = ArrayList()
        val weatherResponseList = ArrayList<Deferred<Response<Weather>>>()
        coroutineScope.launch {
            locations?.forEach { location ->
                val weatherDeferred = coroutineScope.async(Dispatchers.IO) {
                    weatherApiService.getLocationWeatherAsync(location.whereOnEarthId)
                }
                weatherResponseList.add(weatherDeferred)
            }
            weatherResponseList.forEach{ weatherDeferredResponse ->
                val weatherResponse = weatherDeferredResponse.await()
                if (!weatherResponse.isSuccessful) {
                    throw RuntimeException("Failed to request weather. code: ${weatherResponse.code()}, message: ${weatherResponse.message()}")
                }
                weatherResponse.body()?.let { newWeathers.add(it) }
            }
        }.join()
        return newWeathers
    }
    suspend fun getLocation(coroutineScope: CoroutineScope, location: String) : List<Location>? {
        var newLocations: List<Location>? = null
        coroutineScope.launch(Dispatchers.IO) {
            val locationsResponse = weatherApiService.getLocationSearchAsync(location)
            if (!locationsResponse.isSuccessful) {
                throw RuntimeException("Failed to request locations. code: ${locationsResponse.code()}, message: ${locationsResponse.message()}")
            }
            newLocations = locationsResponse.body()
        }.join()
        return newLocations
    }
}