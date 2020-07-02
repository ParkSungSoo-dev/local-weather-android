package dev.soaptree.local.weather.data

import com.google.gson.annotations.SerializedName
import dev.soaptree.local.weather.util.Utils

data class LocationSearched(
    @SerializedName("title")
    val title: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("woeid")
    val whereOnEarthId: Int,
    @SerializedName("latt_long")
    private val lattLong: String?
) {
    val latitude: Double
        get() {
            Utils.parseDouble(lattLong, LATT_LONG_SEPARATOR_REGEX, INDEX_OF_LATITUDE)?.let {
                return it
            }
            return -0.1
        }
    val longitude: Double
        get() {
            Utils.parseDouble(lattLong, LATT_LONG_SEPARATOR_REGEX, INDEX_OF_LONGITUDE)?.let {
                return it
            }
            return -1.0
        }

    companion object {
        private const val LATT_LONG_SEPARATOR_REGEX = ","
        private const val INDEX_OF_LATITUDE = 0
        private const val INDEX_OF_LONGITUDE = 1
    }
}