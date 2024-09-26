import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.challenge.virtusa_weather.viewmodel.WeatherViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val weather by viewModel.weatherState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (city.isNotEmpty()) {
                viewModel.fetchWeather(city)
            }
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show loading indicator
        if (isLoading) {
            CircularProgressIndicator()
        }

        // Show weather data
        weather?.let {
            Text("City: ${it.cityName}")
            Text("Temperature: ${it.main.temp}")
            Text("Description: ${it.weather[0].description}")

            val iconUrl = "https://openweathermap.org/img/wn/${it.weather[0].icon}.png"
            Image(
                painter = rememberAsyncImagePainter(iconUrl),
                contentDescription = null,
                modifier = Modifier.height(50.dp) // Adjust size if needed
            )
        }

        // Show error message if there's an error
        if (error.isNotEmpty()) {
            Text(error, color = Color.Red)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreen() {
    WeatherScreenPreviewContent()
}

@Composable
fun WeatherScreenPreviewContent() {
    // Simulate some data to preview
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = "New York",
            onValueChange = {},
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("City: New York")
        Text("Temperature: 20°C")
        Text("Description: Clear Sky")

        val iconUrl = "https://openweathermap.org/img/wn/01d.png"
        Image(
            painter = rememberAsyncImagePainter(iconUrl),
            contentDescription = null,
            modifier = Modifier.height(50.dp)
        )
    }
}
