package in2000.pedalio.domain.routing

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.domain.weather.GetWeatherUseCase
import in2000.pedalio.domain.weather.WeatherDataPoint

class GetWeatherOnRouteUseCase(private val weatherRepository: WeatherRepository, private val getWeatherUseCase : GetWeatherUseCase) {
    suspend fun getBatchWeather(locations: List<LatLng>) : List<WeatherDataPoint>? {
        val weatherDataPoints = mutableListOf<WeatherDataPoint>()
        for (location in locations) {
            val weatherDataPoint = getWeatherUseCase.getWeather(location)
            if (weatherDataPoint != null) {
                weatherDataPoints.add(weatherDataPoint)
            } else {
                return null // this should never happen
            }
        }
        return weatherDataPoints
    }
}

