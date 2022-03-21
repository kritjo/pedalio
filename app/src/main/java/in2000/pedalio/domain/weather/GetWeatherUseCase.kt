package in2000.pedalio.domain.weather

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.weather.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    suspend fun getWeather(latLng: LatLng, timeDelta : Int = 0): WeatherDataPoint? {
        val temp = weatherRepository.getTemp(latLng.latitude, latLng.longitude, timeDelta)
        val percipitation = weatherRepository.getPercipitation(latLng.latitude, latLng.longitude, timeDelta)
        val humidity = weatherRepository.getPercipitation(latLng.latitude, latLng.longitude, timeDelta)
        return WeatherDataPoint(temp, percipitation, humidity)
    }
}


data class WeatherDataPoint(val temperature: Double?, val percipitation: Double?, val humidity: Double?) // consider moving this to a separate file