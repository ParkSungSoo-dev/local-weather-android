package dev.soaptree.local.weather.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class Weather(
    @SerializedName("title")
    val title: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("time")
    val time: Date,
    @SerializedName("sun_rise")
    val sunRiseTime: Date,
    @SerializedName("sun_set")
    val sunSetTime: Date,
    @SerializedName("parent")
    val parent: Location,
    @SerializedName("consolidated_weather")
    val consolidatedWeathers: List<ConsolidatedWeather>,
    @SerializedName("sources")
    val soruces: List<Source>
) {
    data class ConsolidatedWeather (
        @SerializedName("id")
        val id: Long,
        @SerializedName("applicable_date")
        val applicableDate: Date,
        @SerializedName("weather_state_name")
        val weatherStateName: String,
        @SerializedName("weather_state_abbr")
        val weatherStateAbbr: String,
        @SerializedName("wind_speed")
        val windSpeed: Double,
        @SerializedName("wind_direction")
        val windDirection: Double,
        @SerializedName("wind_direction_compass")
        val windDirectionCompass: String,
        @SerializedName("min_temp")
        val minTemp: Double,
        @SerializedName("max_temp")
        val maxTemp: Double,
        @SerializedName("the_temp")
        val currentTemp: Double,
        @SerializedName("air_pressure")
        val airPressure: Double,
        @SerializedName("humidity")
        val humidity: Double,
        @SerializedName("visibility")
        val visibility: Double,
        @SerializedName("predictability")
        val predictability: Int
    )

    data class Source (
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String
    )

    val todayWeather: ConsolidatedWeather?
    get() {
        return if (consolidatedWeathers.isNotEmpty()) {
            consolidatedWeathers[0]
        } else {
            null
        }
    }
    val tomorrowWeather: ConsolidatedWeather?
        get() {
            return if (consolidatedWeathers.size >= 2) {
                consolidatedWeathers[1]
            } else {
                null
            }
        }
}