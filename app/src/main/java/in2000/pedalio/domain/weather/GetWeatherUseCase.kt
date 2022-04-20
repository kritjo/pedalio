package in2000.pedalio.domain.weather

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.search.impl.ReverseGeocodingRepository
import in2000.pedalio.data.weather.impl.LocationforecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository

class GetWeatherUseCase(val nowcastRepository: NowcastRepository, val locationforecastRepository: LocationforecastRepository) {
    /**
     * Get the weather for the current location.
     * @return WeatherDataPoint data class for the current location
     * @param latLng the current location
     * @param timeDelta the time delta in minutes
     */
    suspend fun getWeather(latLng: LatLng, timeDelta : Int = 0, context: Context): WeatherDataPoint {
        assert(timeDelta >= 0)
        if (ReverseGeocodingRepository(context).getCountry(latLng) != "NOR") {
            return WeatherDataPoint(latLng, 0.0, 0.0, 0.0, 0.0, 0.0, null)

        }

        return when {
            timeDelta == 0 -> {
                val temp = nowcastRepository.getTemp(latLng.latitude, latLng.longitude, 0)
                val percipitation = nowcastRepository.getPercipitation(latLng.latitude, latLng.longitude, 0)
                val humidity = nowcastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, 0)
                val windSpeed = nowcastRepository.getWindSpeed(latLng.latitude, latLng.longitude, 0)
                val windDirection = nowcastRepository.getWindDirection(latLng.latitude, latLng.longitude, 0)
                val symbolCode = nowcastRepository.getWeatherIcon(latLng.latitude, latLng.longitude, 0)
                WeatherDataPoint(latLng, temp, percipitation, humidity, windSpeed, windDirection, symbolCode)
            }
            timeDelta <= 60 -> {
                val temp = locationforecastRepository.getTemp(latLng.latitude, latLng.longitude, 0)
                val percipitation = nowcastRepository.getPercipitation(latLng.latitude, latLng.longitude, timeDelta) // using nowcast as it is more accurate
                val humidity = locationforecastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, 0)
                val windSpeed = nowcastRepository.getWindSpeed(latLng.latitude, latLng.longitude, 0)
                val windDirection = nowcastRepository.getWindDirection(latLng.latitude, latLng.longitude, 0)
                val symbolCode = nowcastRepository.getWeatherIcon(latLng.latitude, latLng.longitude, 0)
                WeatherDataPoint(latLng, temp, percipitation, humidity, windSpeed, windDirection, symbolCode)
            }
            else -> {
                val temp = locationforecastRepository.getTemp(latLng.latitude, latLng.longitude, timeDelta)
                val percipitation = locationforecastRepository.getPercipitation(latLng.latitude, latLng.longitude, timeDelta)
                val humidity = locationforecastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, timeDelta)
                val windSpeed = locationforecastRepository.getWindSpeed(latLng.latitude, latLng.longitude, timeDelta)
                val windDirection = locationforecastRepository.getWindDirection(latLng.latitude, latLng.longitude, timeDelta)
                val symbolCode = locationforecastRepository.getWeatherIcon(latLng.latitude, latLng.longitude, timeDelta)
                WeatherDataPoint(latLng, temp, percipitation, humidity, windSpeed, windDirection, symbolCode)
            }
        }
    }
}