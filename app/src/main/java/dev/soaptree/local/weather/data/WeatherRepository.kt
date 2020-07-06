package dev.soaptree.local.weather.data

import android.util.Log
import dev.soaptree.local.weather.data.remote.WeatherApiService
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.RuntimeException

object WeatherRepository {
    private val weatherApiService = WeatherApiService.create()
    @Throws(RuntimeException::class)
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

    @Throws(RuntimeException::class)
    suspend fun getLocationWeathers(coroutineScope: CoroutineScope, locationSearchedList: List<LocationSearched>?) : ArrayList<LocationWeather> {
        val newLocationWeathers: ArrayList<LocationWeather> = ArrayList()
        val locationWeatherDeferredResponseList = ArrayList<Deferred<Response<LocationWeather>>>()
        coroutineScope.launch {
            locationSearchedList?.forEach { location ->
                supervisorScope {
                    val weatherDeferred = coroutineScope.async(Dispatchers.IO) {
                        weatherApiService.getLocationWeather(location.whereOnEarthId)
                    }
                    locationWeatherDeferredResponseList.add(weatherDeferred)
                }
            }
            locationWeatherDeferredResponseList.forEach{ weatherDeferredResponse ->
                val newWeather = try {
                    val weatherResponse = weatherDeferredResponse.await()
                    if (!weatherResponse.isSuccessful) {
                        throw RuntimeException("Failed to getLocationWeather. code: ${weatherResponse.code()}, message: ${weatherResponse.message()}")
                    }
                    weatherResponse.body()
                } catch (e: Exception) {
                    Log.e("test_code", "exception on getLocationWeather", e)
                    null
                }
                newWeather?.let {
                    newLocationWeathers.add(it)
                }
            }
        }.join()
        return newLocationWeathers
    }
}