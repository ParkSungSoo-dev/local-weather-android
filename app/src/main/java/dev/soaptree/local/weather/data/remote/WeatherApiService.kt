package dev.soaptree.local.weather.data.remote

import com.google.gson.GsonBuilder
import dev.soaptree.local.weather.BuildConfig
import dev.soaptree.local.weather.data.Weather
import dev.soaptree.local.weather.data.Location
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.DateFormat

interface WeatherApiService {
    @GET("location/search")
    suspend fun getLocationSearchAsync(
        @Query("query")
        locationName: String
    ) : Response<List<Location>>

    @GET("location/{woeid}")
    suspend fun getLocationWeatherAsync(
        @Path("woeid")
        whereOnEarthId: Int
    ) : Response<Weather>

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