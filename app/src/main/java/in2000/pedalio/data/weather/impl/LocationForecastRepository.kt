package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.locationforecast.LocationForecastSource
import kotlin.math.floor

/**
 * Implementation of Weather using LocationForecast API. Provides no caching.
 * Source of truth: LocationForecast API.
 * @property endpoint
 */
class LocationForecastRepository(
    private val endpoint: String
) : WeatherRepository() {

    /**
     * @see [WeatherRepository.getTemp]
     */
    override suspend fun getTemp(lat: Double,
                         lon: Double,
                         timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.air_temperature

    }

    /**
     * @see [WeatherRepository.getPrecipitationRate]
     */
    override suspend fun getPrecipitationRate(lat: Double,
                                              lon: Double,
                                              timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_amount
    }

    /**
     * @see [WeatherRepository.getPrecipitation]
     */
    override suspend fun getPrecipitation(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_amount

    }

    /**
     * @see [WeatherRepository.getRelativeHumidity]
     */
    override suspend fun getRelativeHumidity(lat: Double,
                                             lon: Double,
                                             timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.relative_humidity

    }

    /**
     * @see [WeatherRepository.getWindDirection]
     */
    override suspend fun getWindDirection(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_from_direction

    }

    /**
     * @see [WeatherRepository.getWindSpeed]
     */
    override suspend fun getWindSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed

    }

    /**
     * @see [WeatherRepository.getGustSpeed]
     */
    override suspend fun getGustSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed_of_gust
    }

    /**
     * @see [WeatherRepository.radarCoverage]
     */
    override suspend fun radarCoverage(lat: Double,
                                       lon: Double): Boolean {
        throw UnsupportedOperationException("Not implemented by LocationForecast")
    }

    /**
     * @see [WeatherRepository.getWeatherIcon]
     */
    override suspend fun getWeatherIcon(lat: Double, lon: Double, timeDelta: Int): String? {
        val forecast = LocationForecastSource
            .getLocationforecast(endpoint, lat, lon)
        if (forecast != null) {
            if ((timeDelta / 60) >= forecast.properties?.timeseries?.size ?: 0)
                throw IllegalStateException("No time available")
        } else {
            return null
        }
        return  forecast
            .properties
            ?.timeseries?.get(floor((timeDelta / 60).toDouble()).toInt())
            ?.data
            ?.next_1_hours
            ?.summary
            ?.symbol_code
    }
}