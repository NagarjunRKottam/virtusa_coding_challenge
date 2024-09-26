package com.challenge.virtusa_weather.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.virtusa_weather.data.WeatherResponse
import com.challenge.virtusa_weather.services.WeatherAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherAPI: WeatherAPI) : ViewModel() {

    // StateFlow for weather data
    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    // StateFlow for loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // StateFlow for error messages
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = weatherAPI.getWeatherByCity(city)
                if (response.isSuccessful) {
                    _weatherState.value = response.body()
                    _errorMessage.value = ""
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    _errorMessage.value = "Error: $errorMsg"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

