package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.nowcast.NowcastSource
import kotlinx.coroutines.runBlocking
import kotlin.math.floor

/**
 * Implementation of Weather using Nowcast API. Provides no caching.
 * Source of truth: Nowcast API.
 */
class NowcastRepository(
    private val endpoint: String
) : WeatherRepository() {
    override suspend fun getTemp(lat: Double,
                                 lon: Double,
                                 timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .air_temperature

    }

    override suspend fun getPercipitationRate(lat: Double,
                                              lon: Double,
                                              timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .precipitation_rate

    }

    override suspend fun getPercipitation(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .precipitation_amount
    }

    override suspend fun getRelativeHumidity(lat: Double,
                                             lon: Double,
                                             timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .relative_humidity
    }

    override suspend fun getWindDirection(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_from_direction
    }

    override suspend fun getWindSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_speed

    }

    override suspend fun getGustSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        return NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .timeseries[floor((timeDelta / 5).toDouble()).toInt()]
                .data
                .instant
                .details
                .wind_speed_of_gust
    }

    override suspend fun radarCoverage(lat: Double,
                                       lon: Double): Boolean {
        val cov = NowcastSource
                .getNowcast(endpoint, lat, lon)
                .properties
                .meta
                .radar_coverage

        if (cov == "ok") return true
        return false
    }
}