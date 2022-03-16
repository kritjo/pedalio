package in2000.pedalio.data.airquality.impl

import in2000.pedalio.data.airquality.AirQualityRepository
import in2000.pedalio.data.airquality.source.NILU.NILUSource
import java.lang.UnsupportedOperationException

/**
 * Air Quality implementation from NILU API, shows last recorded value.
 * @property endpoint NILU API endpoint for aq api mode
 * @property radius Radius of stations to take average of
 */
class NILURepository(val endpoint: String, val radius: Int): AirQualityRepository() {
    override suspend fun getNO2(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.NO2)
        val noxSum = res.sumOf { it.value!! }
        return noxSum / res.size
    }

    override suspend fun getPM10(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.PM10)
        val pm10Sum = res.sumOf { it.value!! }
        return pm10Sum / res.size
    }

    override suspend fun getPM25(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.PM2_5)
        val pm25Sum = res.sumOf { it.value!! }
        return pm25Sum / res.size
    }

    override suspend fun getAQI(lat: Double, lon: Double, timeDelta: Int): Double {
        if (timeDelta != 0) throw UnsupportedOperationException("NILU only provides now")
        val res = NILUSource.getNow(endpoint, lat, lon, radius, NILUSource.COMPONENTS.ALL)
        val aqiSum = res.sumOf { it.index!! }
        val aqiAvg: Double = aqiSum.toDouble() / res.size.toDouble()
        return (aqiAvg / 4) * 5
    }
}