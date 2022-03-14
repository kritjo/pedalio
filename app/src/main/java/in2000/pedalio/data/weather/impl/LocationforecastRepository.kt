package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.locationforecast.LocationforecastSource
import kotlinx.coroutines.runBlocking
import kotlin.math.floor

/**
 * Implementation of Weather using LocationForecast API. Provides no caching.
 * Source of truth: LocationForecast API.
 * TODO: Discuss whether we should use runBlocking or suspend or something else.
 * @property endpoint
 */
class LocationforecastRepository(
    private val endpoint: String
) : WeatherRepository() {

    override fun getTemp(lat: Double,
                         lon: Double,
                         timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .instant
                .details
                .air_temperature
        }
    }

    override fun getPercipitationRate(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .next_1_hours
                ?.details
                ?.precipitation_amount
        }
    }

    override fun getPercipitation(lat: Double,
                                  lon: Double,
                                  timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .instant
                .details
                .precipitation_amount
        }
    }

    override fun getRelativeHumidity(lat: Double,
                                     lon: Double,
                                     timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .instant
                .details
                .relative_humidity
        }
    }

    override fun getWindDirection(lat: Double,
                                  lon: Double,
                                  timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_from_direction
        }
    }

    override fun getWindSpeed(lat: Double,
                              lon: Double,
                              timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_speed
        }
    }

    override fun getGustSpeed(lat: Double,
                              lon: Double,
                              timeDelta: Int): Double? {
        return runBlocking {
            LocationforecastSource
                .getLocationforecast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 60).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_speed_of_gust
        }
    }

    override fun radarCoverage(lat: Double,
                               lon: Double): Boolean {
        throw UnsupportedOperationException("Not implemented by Locationforecast")
    }
}