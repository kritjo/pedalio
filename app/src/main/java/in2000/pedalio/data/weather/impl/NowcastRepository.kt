package in2000.pedalio.data.weather.impl

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.weather.WeatherRepository
import in2000.pedalio.data.weather.source.nowcast.NowcastCompleteDataClass
import in2000.pedalio.data.weather.source.nowcast.NowcastSource
import kotlin.math.floor

/**
 * Implementation of Weather using Nowcast API. Provides no caching.
 * Source of truth: Nowcast API.
 */
class NowcastRepository(
    private val endpoint: String
) : WeatherRepository() {
    var source: Pair<NowcastCompleteDataClass, LatLng>? = null

    /**
     * @see [WeatherRepository.getTemp]
     */
    override suspend fun getTemp(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.air_temperature
    }

    /**
     * @see [WeatherRepository.getPrecipitationRate]
     */
    override suspend fun getPrecipitationRate(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.precipitation_rate

    }

    /**
     * @see [WeatherRepository.getPrecipitation]
     */
    override suspend fun getPrecipitation(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
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

    /**
     * @see [WeatherRepository.getRelativeHumidity]
     */
    override suspend fun getRelativeHumidity(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.relative_humidity
    }

    /**
     * @see [WeatherRepository.getWindDirection]
     */
    override suspend fun getWindDirection(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_from_direction
    }

    /**
     * @see [WeatherRepository.getWindSpeed]
     */
    override suspend fun getWindSpeed(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed

    }

    /**
     * @see [WeatherRepository.getGustSpeed]
     */
    override suspend fun getGustSpeed(
        lat: Double,
        lon: Double,
        timeDelta: Int
    ): Double? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
        }
        return source!!.first
            .properties
            ?.timeseries?.get(floor((timeDelta / 5).toDouble()).toInt())
            ?.data
            ?.instant
            ?.details
            ?.wind_speed_of_gust
    }

    /**
     * @see [WeatherRepository.radarCoverage]
     */
    override suspend fun radarCoverage(
        lat: Double,
        lon: Double
    ): Boolean {
        val nc = NowcastSource
            .getNowcast(endpoint, lat, lon) ?: return false

        val cov = nc
            .properties
            ?.meta
            ?.radar_coverage

        if (cov == "ok") return true
        return false
    }

    /**
     * @see [WeatherRepository.getWeatherIcon]
     */
    override suspend fun getWeatherIcon(lat: Double, lon: Double, timeDelta: Int): String? {
        if (source?.second?.latitude != lat && source?.second?.longitude != lon) {
            source =
                Pair(NowcastSource.getNowcast(endpoint, lat, lon) ?: return null, LatLng(lat, lon))
        }
        if (source == null) {
            return null
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