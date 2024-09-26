package com.challenge.virtusa_weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.virtusa_weather.data.Main
import com.challenge.virtusa_weather.data.WeatherInfo
import com.challenge.virtusa_weather.data.WeatherResponse
import com.challenge.virtusa_weather.services.WeatherAPI
import com.challenge.virtusa_weather.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var weatherAPI: WeatherAPI
    private lateinit var weatherViewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        weatherAPI = mock(WeatherAPI::class.java)
        weatherViewModel = WeatherViewModel(weatherAPI)
    }

    @Test
    fun `fetchWeather updates weatherState with successful response`() = runTest {
        val mockResponse = WeatherResponse(
            cityName = "Tampa",
            main = Main(temp = 30.5f),
            weather = listOf(WeatherInfo(description = "Clear sky", icon = "01d"))
        )
        val response = Response.success(mockResponse)
        `when`(weatherAPI.getWeatherByCity("Tampa")).thenReturn(response)

        val job = launch {
            weatherViewModel.fetchWeather("Tampa")
        }
        job.join()

        assertEquals(mockResponse, weatherViewModel.weatherState.first())
        assertEquals(false, weatherViewModel.isLoading.first())
        assertEquals("", weatherViewModel.errorMessage.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchWeather updates errorMessage on failed response`() = runTest {
        // Arrange
        val errorResponse = Response.error<WeatherResponse>(
            404,
            ResponseBody.create(null, "Not Found")
        )
        `when`(weatherAPI.getWeatherByCity("UnknownCity")).thenReturn(errorResponse)

        // Act
        weatherViewModel.fetchWeather("UnknownCity")
        advanceUntilIdle()

        // Assert
        assertEquals("Error: Not Found", weatherViewModel.errorMessage.first())
        assertNull(weatherViewModel.weatherState.first())
        assertFalse(weatherViewModel.isLoading.first())
    }


    @Test
    fun `fetchWeather updates errorMessage on exception`() = runTest {
        `when`(weatherAPI.getWeatherByCity("Tampa")).thenThrow(RuntimeException("Network Error"))

        val job = launch {
            weatherViewModel.fetchWeather("Tampa")
        }
        job.join()

        assertEquals("Exception: Network Error", weatherViewModel.errorMessage.first())
        assertEquals(null, weatherViewModel.weatherState.first())
        assertEquals(false, weatherViewModel.isLoading.first())
    }
}

