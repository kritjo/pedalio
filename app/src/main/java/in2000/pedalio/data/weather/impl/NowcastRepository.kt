package in2000.pedalio.data.weather.impl

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.nowcast.NowcastCompleteDataClass
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
    var source: Pair<NowcastCompleteDataClass, LatLng>? = null

    override suspend fun getTemp(lat: Double,
                                 lon: Double,
                                 timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.air_temperature
    }

    override suspend fun getPercipitationRate(lat: Double,
                                              lon: Double,
                                              timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_rate

    }

    override suspend fun getPercipitation(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        val cast = source!!.first
        return cast.properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_amount
            // If there is no precipitation data at good resolution, use the hourly data
            ?: cast.properties
                ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
                ?.data
                ?.next_1_hours
                ?.details
                ?.precipitation_amount
    }

    override suspend fun getRelativeHumidity(lat: Double,
                                             lon: Double,
                                             timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.relative_humidity
    }

    override suspend fun getWindDirection(lat: Double,
                                          lon: Double,
                                          timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_from_direction
    }

    override suspend fun getWindSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed

    }

    override suspend fun getGustSpeed(lat: Double,
                                      lon: Double,
                                      timeDelta: Int): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed_of_gust
    }

    override suspend fun radarCoverage(lat: Double,
                                       lon: Double): Boolean {
        val cov = NowcastSource
            .getNowcast(endpoint, lat, lon)
            .properties
            ?.meta
            ?.radar_coverage

        if (cov == "ok") return true
        return false
    }

    override suspend fun getWeatherIcon(lat: Double, lon: Double, timeDelta: Int): String? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source = Pair(NowcastSource.getNowcast(endpoint, lat, lon), LatLng(lat, lon))
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.next_1_hours
            ?.summary
            ?.symbol_code
    }
}