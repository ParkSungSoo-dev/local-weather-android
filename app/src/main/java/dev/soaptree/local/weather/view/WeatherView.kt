package dev.soaptree.local.weather.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import dev.soaptree.local.weather.BuildConfig
import dev.soaptree.local.weather.R
import kotlinx.android.synthetic.main.view_weather.view.*

class WeatherView : ConstraintLayout {
    constructor(context: Context) : super(context) {
        initView(context, null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_weather, this, true)
        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Weather, 0, 0)
            typedArray.getString(R.styleable.Weather_stateName)?.let { stateName ->
                textview_weather_state.text = stateName
            }
            typedArray.getString(R.styleable.Weather_stateAbbr)?.let { stateAbbr ->
                Glide.with(this).load(getWeatherImageUrl(stateAbbr)).into(imageview_weather_state)
            }
            typedArray.getString(R.styleable.Weather_temp)?.let { temp ->
                textview_temp.text = temp
            }
            typedArray.getString(R.styleable.Weather_humidity)?.let { humidity ->
                textview_humidity.text = humidity
            }
            typedArray.recycle()
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

        fun getWeatherImageUrl(weatherStateAbbr: String) : String {
            return BuildConfig.WEATHER_IMAGE_URL_FORMAT.format(weatherStateAbbr)
        }
    }
}