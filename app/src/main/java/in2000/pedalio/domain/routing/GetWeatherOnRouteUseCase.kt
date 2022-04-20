package in2000.pedalio.domain.routing

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.domain.weather.GetWeatherUseCase
import in2000.pedalio.domain.weather.WeatherDataPoint


class GetWeatherOnRouteUseCase(private val getWeatherUseCase : GetWeatherUseCase, private val context: Context) {
    /**
     * Use case to get weather data for a route.
     * @param locations List of locations to get weather data for.
     * @return Weather data for a route.
     */
    suspend fun getBatchWeather(locations: List<LatLng>) : List<WeatherDataPoint> {
        val weatherDataPoints = mutableListOf<WeatherDataPoint>()
        for (location in locations) {
            val weatherDataPoint = getWeatherUseCase.getWeather(location, context = context)
            weatherDataPoints.add(weatherDataPoint)
        }
        return weatherDataPoints
    }
}

