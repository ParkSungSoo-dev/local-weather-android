package dev.soaptree.local.weather.repository

import com.google.gson.GsonBuilder
import dev.soaptree.local.weather.BuildConfig
import dev.soaptree.local.weather.Location
import dev.soaptree.local.weather.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.DateFormat

// TODO: change to coroutine
interface WeatherApiService {
    @GET("location/search")
    fun requestLocationSearch(
        @Query("query")
        locationName: String
    ) : Call<List<Location>>

    @GET("location/{woeid}")
    fun requestLocationWeather(
        @Path("woeid")
        whereOnEarthId: Int
    ) : Call<Weather>


    companion object Factory {
        private val retrofit: Retrofit by lazy {
            val gson = GsonBuilder()
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .create()
            Retrofit.Builder()
                .baseUrl(BuildConfig.WEATHER_SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        fun create() : WeatherApiService {
            return retrofit.create(
                WeatherApiService::class.java)
        }
    }
}