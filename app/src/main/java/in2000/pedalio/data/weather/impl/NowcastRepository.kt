package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.NowcastSource
import kotlinx.coroutines.runBlocking
import kotlin.math.floor

/**
 * Implementation of Weather using Nowcast API. Provides no caching.
 * Source of truth: Nowcast API.
 */
class NowcastRepository(
    private val endpoint: String
) : WeatherRepository() {
    override fun getTemp(lat: Double,
                         lon: Double,
                         timeDelta: Int): Double? {
        return runBlocking {
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
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
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .precipitation_rate
        }
    }

    override fun getPercipitation(lat: Double,
                                  lon: Double,
                                  timeDelta: Int): Double? {
        return runBlocking {
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
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
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
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
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
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
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
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
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_speed_of_gust
        }
    }

    override fun radarCoverage(lat: Double,
                               lon: Double): Boolean {
        val cov = runBlocking {
            NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .meta
                .radar_coverage
        }
        if (cov == "ok") return true
        return false
    }
}