package com.challenge.virtusa_weather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<WeatherInfo>
)

data class Main(
    @SerializedName("temp") val temp: Float
)

data class WeatherInfo(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)
