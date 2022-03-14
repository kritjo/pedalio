package in2000.pedalio.data.airquality

abstract class AirQualityRepository {

    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets NOX value for the given location and timeDelta in ug/m3
     */
    abstract suspend fun getNOX(lat: Double, lon: Double, timeDelta: Int = 0): Double

    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets Particulate Matter value for the given location and timeDelta in ug/m3
     */
    abstract suspend fun getPM10(lat: Double, lon: Double, timeDelta: Int = 0): Double

    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets Particulate Matter value for the given location and timeDelta in ug/m3
     */
    abstract suspend fun getPM25(lat: Double, lon: Double, timeDelta: Int = 0): Double

    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets Air Quality Index value for the given location and timeDelta from 1 (min) to 5 (max)
     */
    abstract suspend fun getAQI(lat: Double, lon: Double, timeDelta: Int = 0): Double
}