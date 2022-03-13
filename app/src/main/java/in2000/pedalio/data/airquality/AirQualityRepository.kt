package in2000.pedalio.data.airquality

abstract class AirQualityRepository {

    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets NOX value for the given location and timeDelta
     */
    abstract fun getNOX(lat: Double, lon: Double, timeDelta: Int = 0): Double
    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets Particulate Matter value for the given location and timeDelta
     */
    abstract fun getPM(lat: Double, lon: Double, timeDelta: Int = 0): Double
    /**
     * @param lat latitude
     * @param lon longitude
     * @param timeDelta in minutes
     * @return Gets Air Quality Index value for the given location and timeDelta
     */
    abstract fun getAQI(lat: Double, lon: Double, timeDelta: Int = 0): Double
}