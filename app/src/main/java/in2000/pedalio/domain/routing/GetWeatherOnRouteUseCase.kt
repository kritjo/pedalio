package in2000.pedalio.domain.routing

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.domain.weather.GetWeatherUseCase
import in2000.pedalio.domain.weather.WeatherDataPoint


class GetWeatherOnRouteUseCase(private val getWeatherUseCase : GetWeatherUseCase) {
    /**
     * Use case to get weather data for a route.
     * @param getWeatherUseCase Use case to get weather data for a location.
     * @return Weather data for a route.
     */
    suspend fun getBatchWeather(locations: List<LatLng>) : List<WeatherDataPoint>? {
        val weatherDataPoints = mutableListOf<WeatherDataPoint>()
        for (location in locations) {
            val weatherDataPoint = getWeatherUseCase.getWeather(location)
            weatherDataPoints.add(weatherDataPoint)
        }
        return weatherDataPoints
    }
}

