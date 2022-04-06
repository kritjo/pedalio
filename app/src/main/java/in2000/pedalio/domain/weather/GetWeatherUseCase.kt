package in2000.pedalio.domain.weather

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.weather.impl.LocationforecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository

class GetWeatherUseCase(val nowcastRepository: NowcastRepository, val locationforecastRepository: LocationforecastRepository) {
    /**
     * Get the weather for the current location.
     * @return WeatherDataPoint data class for the current location
     * @param latLng the current location
     * @param timeDelta the time delta in minutes
     */
    suspend fun getWeather(latLng: LatLng, timeDelta : Int = 0): WeatherDataPoint {
        assert(timeDelta >= 0)

        return if(timeDelta == 0) {
            val temp = nowcastRepository.getTemp(latLng.latitude, latLng.longitude, 0)
            val percipitation = nowcastRepository.getPercipitation(latLng.latitude, latLng.longitude, 0)
            val humidity = nowcastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, 0)
            WeatherDataPoint(temp, percipitation, humidity)
        } else if(timeDelta <= 60){
            val temp = locationforecastRepository.getTemp(latLng.latitude, latLng.longitude, 0)
            val percipitation = nowcastRepository.getPercipitation(latLng.latitude, latLng.longitude, timeDelta) // using nowcast as it is more accurate
            val humidity = locationforecastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, 0)
            WeatherDataPoint(temp, percipitation, humidity)
        } else {
            val temp = locationforecastRepository.getTemp(latLng.latitude, latLng.longitude, timeDelta)
            val percipitation = locationforecastRepository.getPercipitation(latLng.latitude, latLng.longitude, timeDelta)
            val humidity = locationforecastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, timeDelta)
            WeatherDataPoint(temp, percipitation, humidity)
        }
    }
}


data class WeatherDataPoint(val temperature: Double?, val percipitation: Double?, val humidity: Double?) // consider moving this to a separate file
