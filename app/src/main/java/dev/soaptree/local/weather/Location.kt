package dev.soaptree.local.weather

import com.google.gson.annotations.SerializedName
import dev.soaptree.local.weather.util.Utils

private const val LATT_LONG_SEPARATOR_REGEX = ","
private const val INDEX_OF_LATITUDE = 0
private const val INDEX_OF_LONGITUDE = 0

data class Location(
    @SerializedName("title")
    val title: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("woeid")
    val whereOnEarthId: Int,
    @SerializedName("latt_long")
    private var lattLong: String?
) {
    // TODO: create new class for latitude/longitude
    val latitude: Double
        get() {
            var latitude = 0.0
            Utils.parseDouble(lattLong, LATT_LONG_SEPARATOR_REGEX, INDEX_OF_LATITUDE)?.let {
                latitude = it
            }
            return latitude
        }
    val longitude: Double
        get() {
            var longitude = 0.0
            Utils.parseDouble(lattLong, LATT_LONG_SEPARATOR_REGEX, INDEX_OF_LONGITUDE)?.let {
                longitude = it
            }
            return longitude
        }
}