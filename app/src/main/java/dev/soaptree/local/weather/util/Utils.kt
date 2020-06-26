package dev.soaptree.local.weather.util

import android.content.Context
import dev.soaptree.local.weather.R

class Utils {

    companion object {
        fun parseDouble(srcText: String?, separator: String, index: Int): Double? {
            srcText?.let { srcTextNotNull ->
                val srcTextSplit = srcTextNotNull.split(separator.toRegex())
                if (srcTextSplit.size >= index) {
                    return srcTextSplit[index].toDouble()
                }
            }
            return null
        }

        @JvmStatic
        fun temperatureWithSymbol(context: Context, temperature: Double): String {
            return context.resources.getString(R.string.format_temp).format(temperature)
        }
        @JvmStatic
        fun humidityWithSymbol(context: Context, humidity: Double): String {
            return context.resources.getString(R.string.format_humidity).format(humidity)
        }
    }
}