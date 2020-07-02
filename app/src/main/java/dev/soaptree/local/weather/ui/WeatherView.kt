package dev.soaptree.local.weather.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import dev.soaptree.local.weather.BuildConfig
import dev.soaptree.local.weather.R
import kotlinx.android.synthetic.main.view_weather.view.*

class WeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_weather, this, true)
        attrs?.let { attributeSet ->
            context.obtainStyledAttributes(attributeSet, R.styleable.WeatherView, 0, 0).apply {
                getString(R.styleable.WeatherView_stateName)?.let { textview_weather_state.text = it }
                getString(R.styleable.WeatherView_stateAbbr)?.let { Glide.with(this@WeatherView).load(getWeatherImageUrl(it)).into(imageview_weather_state) }
                getString(R.styleable.WeatherView_temp)?.let { textview_temp.text = it}
                getString(R.styleable.WeatherView_humidity)?.let {  textview_humidity.text = it }
                recycle()
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("stateName")
        fun bindStateName(weatherView: WeatherView, stateName: String) {
            weatherView.textview_weather_state.text = stateName
        }

        @JvmStatic
        @BindingAdapter("stateAbbr")
        fun bindStateAbbr(weatherView: WeatherView, stateAbbr: String) {
            Glide.with(weatherView).load(getWeatherImageUrl(stateAbbr)).into(weatherView.imageview_weather_state)
        }

        @JvmStatic
        @BindingAdapter("temp")
        fun bindTemp(weatherView: WeatherView, temp: String) {
            weatherView.textview_temp.text = temp
        }

        @JvmStatic
        @BindingAdapter("humidity")
        fun bindHumidity(weatherView: WeatherView, humidity: String) {
            weatherView.textview_humidity.text = humidity
        }

        @JvmStatic
        fun getWeatherImageUrl(weatherStateAbbr: String) : String {
            return BuildConfig.WEATHER_IMAGE_URL_FORMAT.format(weatherStateAbbr)
        }
    }
}