package dev.soaptree.local.weather.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.soaptree.local.weather.data.WeatherRepository
import dev.soaptree.local.weather.data.Location
import dev.soaptree.local.weather.data.Weather
import kotlinx.coroutines.*
import java.lang.Exception

class LocationWeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val locations = MutableLiveData<List<Location>>()

    private val _weathers = MutableLiveData<ArrayList<Weather>>()
    val weathers: LiveData<ArrayList<Weather>> = _weathers

    private val _isInitWeathers = MutableLiveData<Boolean>()
    val isInitWeathers: LiveData<Boolean> = _isInitWeathers

    fun searchLocationWeathers(location: String) {
        _isInitWeathers.value = true
        _weathers.value?.let {
            it.clear()
            _weathers.value = it
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                WeatherRepository.getLocation(viewModelScope, location)?.let { newLocations ->
                    withContext(Dispatchers.Main) { locations.value = newLocations }
                    WeatherRepository.getWeathers(viewModelScope, newLocations).let { newWeathers ->
                        withContext(Dispatchers.Main) { _weathers.value = newWeathers }
                    }
                }
            } catch (e: Exception) {
                Log.e("dev.soaptree.local.weather", "searchLocationWeathers", e)
                withContext(Dispatchers.Main) { Toast.makeText(getApplication(), "Failed to searchLocationWeathers", Toast.LENGTH_LONG).show() }
            } finally {
                withContext(Dispatchers.Main) { _isInitWeathers.value = false }
            }
        }
    }
    fun refreshWeathers(onRefreshFinished:() -> Unit) {
        _weathers.value?.let {
            it.clear()
            _weathers.value = it
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newWeathers = WeatherRepository.getWeathers(viewModelScope, locations.value)
                withContext(Dispatchers.Main) { _weathers.value = newWeathers }
            } catch (e: Exception) {
                Log.e("dev.soaptree.local.weather", "refreshWeathers", e)
                withContext(Dispatchers.Main) { Toast.makeText(getApplication(), "Failed to refreshWeathers", Toast.LENGTH_LONG).show() }
            } finally {
                withContext(Dispatchers.Main) { onRefreshFinished() }
            }
        }
    }
}