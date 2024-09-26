package com.challenge.virtusa_weather.screen

import WeatherScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun WeatherNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "weather_screen") {
        composable("weather_screen") {
            WeatherScreen()
        }
    }
}


@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    WeatherNavHost(navController = navController)
}