package dev.soaptree.local.weather.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.soaptree.local.weather.data.WeatherRepository
import dev.soaptree.local.weather.data.LocationSearched
import dev.soaptree.local.weather.data.LocationWeather
import kotlinx.coroutines.*
import java.lang.Exception

class LocationWeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val locationSearchedList = MutableLiveData<List<LocationSearched>>()

    private val _locationWeathers = MutableLiveData<ArrayList<LocationWeather>>()
    val locationWeathers: LiveData<ArrayList<LocationWeather>> = _locationWeathers

    private val _isInitData = MutableLiveData<Boolean>()
    val isInitData: LiveData<Boolean> = _isInitData

    private fun clearLocationWeathers() {
        _locationWeathers.value?.let {
            it.clear()
            _locationWeathers.value = it
        }
    }

    fun searchLocationWeathers(location: String) {
        _isInitData.value = true
        clearLocationWeathers()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("dev.soaptree.local.weather", "searchLocationWeathers", throwable)
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    getApplication(),
                    "Failed to searchLocationWeathers",
                    Toast.LENGTH_LONG
                ).show()
                _isInitData.value = false
            }
        }
        val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)
        scope.launch(Dispatchers.IO) {
            WeatherRepository.getLocationSearchedList(scope, location)?.let { newLocations ->
                withContext(Dispatchers.Main) {
                    locationSearchedList.value = newLocations
                }
            }
            WeatherRepository.getLocationWeathers(scope, locationSearchedList.value).let { newWeathers ->
                withContext(Dispatchers.Main) {
                    _locationWeathers.value = newWeathers
                }
            }
            withContext(Dispatchers.Main) {
                _isInitData.value = false
            }
        }
    }
    fun refreshLocationWeathers(onRefreshFinished:() -> Unit) {
        clearLocationWeathers()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("dev.soaptree.local.weather", "refreshLocationWeathers", throwable)
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(getApplication(), "Failed to refreshWeathers", Toast.LENGTH_LONG).show()
                onRefreshFinished()
            }
        }
        val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)
        scope.launch(Dispatchers.IO) {
            val newWeathers = WeatherRepository.getLocationWeathers(scope, locationSearchedList.value)
            withContext(Dispatchers.Main) {
                _locationWeathers.value = newWeathers
                onRefreshFinished()
            }
        }
    }
}