package in2000.pedalio.domain.weather

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.search.impl.ReverseGeocodingRepository
import in2000.pedalio.data.weather.impl.LocationForecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository

class GetWeatherUseCase(val nowcastRepository: NowcastRepository, val locationForecastRepository: LocationForecastRepository) {
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
                val percipitation = nowcastRepository.getPrecipitation(latLng.latitude, latLng.longitude, 0)
                val humidity = nowcastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, 0)
                val windSpeed = nowcastRepository.getWindSpeed(latLng.latitude, latLng.longitude, 0)
                val windDirection = nowcastRepository.getWindDirection(latLng.latitude, latLng.longitude, 0)
                val symbolCode = nowcastRepository.getWeatherIcon(latLng.latitude, latLng.longitude, 0)
                WeatherDataPoint(latLng, temp, percipitation, humidity, windSpeed, windDirection, symbolCode)
            }
            timeDelta <= 60 -> {
                val temp = locationForecastRepository.getTemp(latLng.latitude, latLng.longitude, 0)
                val percipitation = nowcastRepository.getPrecipitation(latLng.latitude, latLng.longitude, timeDelta) // using nowcast as it is more accurate
                val humidity = locationForecastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, 0)
                val windSpeed = nowcastRepository.getWindSpeed(latLng.latitude, latLng.longitude, 0)
                val windDirection = nowcastRepository.getWindDirection(latLng.latitude, latLng.longitude, 0)
                val symbolCode = nowcastRepository.getWeatherIcon(latLng.latitude, latLng.longitude, 0)
                WeatherDataPoint(latLng, temp, percipitation, humidity, windSpeed, windDirection, symbolCode)
            }
            else -> {
                val temp = locationForecastRepository.getTemp(latLng.latitude, latLng.longitude, timeDelta)
                val percipitation = locationForecastRepository.getPrecipitation(latLng.latitude, latLng.longitude, timeDelta)
                val humidity = locationForecastRepository.getRelativeHumidity(latLng.latitude, latLng.longitude, timeDelta)
                val windSpeed = locationForecastRepository.getWindSpeed(latLng.latitude, latLng.longitude, timeDelta)
                val windDirection = locationForecastRepository.getWindDirection(latLng.latitude, latLng.longitude, timeDelta)
                val symbolCode = locationForecastRepository.getWeatherIcon(latLng.latitude, latLng.longitude, timeDelta)
                WeatherDataPoint(latLng, temp, percipitation, humidity, windSpeed, windDirection, symbolCode)
            }
        }
    }
}