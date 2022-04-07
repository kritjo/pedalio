package in2000.pedalio.data.airquality.impl

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.airquality.AirQualityRepository
import in2000.pedalio.data.airquality.source.NILU.NILUDataItem
import in2000.pedalio.data.airquality.source.NILU.NILUSource
import in2000.pedalio.utils.MathUtil
import java.lang.UnsupportedOperationException

/**
 * Air Quality implementation from NILU API, shows last recorded value.
 * @property endpoint NILU API endpoint for aq api mode
 * @property radius Radius of stations to take average of
 */
class CachedNILURepository(val endpoint: String, val radius: Int): AirQualityRepository() {

    private var res_no2 : List<NILUDataItem>? = null
    private var res_pm10 : List<NILUDataItem>? = null
    private var res_pm25 : List<NILUDataItem>? = null
    private var res_aqi : List<NILUDataItem>? = null

    override suspend fun getNO2(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        if (res_no2 == null) res_no2 = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.NO2)
        val resPair = res_no2!!.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), it.value ?: 0.0) }
        val interpolated = MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
        return interpolated;
    }

    override suspend fun getPM10(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        if (res_pm10 == null) res_pm10 = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.PM10)
        val resPair = res_pm10!!.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), it.value ?: 0.0) }
        val interpolated = MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
        return interpolated;
    }

    override suspend fun getPM25(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        if (res_pm25 == null) res_pm25 = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.PM2_5)
        val resPair = res_pm25!!.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), it.value ?: 0.0) }
        val interpolated = MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
        return interpolated;
    }

    override suspend fun getAQI(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        if (res_aqi == null) res_aqi = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.ALL)
        val resPair : List<Pair<LatLng, Double>> = res_aqi!!.map { Pair(LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0), (it.index ?: 0.0).toDouble()) }
        val interpolated = MathUtil.inverseDistanceWeighting(LatLng(lat, lon), resPair)
        return interpolated;
    }
}