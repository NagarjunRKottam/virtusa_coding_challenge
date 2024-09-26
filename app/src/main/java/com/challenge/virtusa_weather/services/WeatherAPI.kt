package com.challenge.virtusa_weather.services

import com.challenge.virtusa_weather.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "a4abe5382e6ece8961924f255507c85c"
    ): Response<WeatherResponse>
}