package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.locationforecast.LocationforecastSource
import kotlinx.coroutines.runBlocking
import kotlin.math.floor

/**
 * Implementation of Weather using LocationForecast API. Provides no caching.
 * Source of truth: LocationForecast API.
 * @property endpoint
 */
class LocationforecastRepository(
    private val endpoint: String
) : WeatherRepository() {

    override suspend fun getTemp(lat: Double,
                         lon: Double,
                         timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.air_temperature

    }

    override suspend fun getPercipitationRate(lat: Double,
                                              lon: Double,
                                              timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_amount
    }

    override suspend fun getPercipitation(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_amount

    }

    override suspend fun getRelativeHumidity(lat: Double,
                                             lon: Double,
                                             timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.relative_humidity

    }

    override suspend fun getWindDirection(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_from_direction

    }

    override suspend fun getWindSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed

    }

    override suspend fun getGustSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        val forecast = LocationforecastSource
            .getLocationforecast(endpoint, lat, lon)
        if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
            throw IllegalStateException("No time available")
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed_of_gust
    }

    override suspend fun radarCoverage(lat: Double,
                                       lon: Double): Boolean {
        throw UnsupportedOperationException("Not implemented by Locationforecast")
    }
}