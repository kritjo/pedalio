package in2000.pedalio.data.airquality.impl

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.airquality.AirQualityRepository
import in2000.pedalio.data.airquality.source.nilu.NILUSource
import in2000.pedalio.utils.MathUtil

/**
 * Air Quality implementation from NILU API, shows last recorded value. Do not use timeDelta as
 * NILU only provides data for right now.
 * @property endpoint NILU API endpoint for aq api mode
 * @constructor [radius]: Radius of stations to weight
 */
class NILURepository(val endpoint: String, private val radius: Int) : AirQualityRepository() {
    /**
     * @see [AirQualityRepository.getNO2]
     */
    override suspend fun getNO2(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.NO2)
        val resPair =
            res?.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), it.value ?: 0.0) }
                ?: return Double.NaN
        return MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
    }

    /**
     * @see [AirQualityRepository.getPM10]
     */
    override suspend fun getPM10(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.PM10)
        val resPair =
            res?.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), it.value ?: 0.0) }
                ?: return Double.NaN
        return MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
    }

    /**
     * @see [AirQualityRepository.getPM25]
     */
    override suspend fun getPM25(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.PM2_5)
        val resPair =
            res?.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), it.value ?: 0.0) }
                ?: return Double.NaN
        return MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
    }

    /**
     * @see [AirQualityRepository.getAQI]
     */
    override suspend fun getAQI(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.ALL)
        val resPair: List<Pair<LatLng, Double>> =
            res?.map {
                Pair(
                    LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0),
                    (it.index ?: 0.0).toDouble()
                )
            } ?: return Double.NaN
        return MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
    }
}