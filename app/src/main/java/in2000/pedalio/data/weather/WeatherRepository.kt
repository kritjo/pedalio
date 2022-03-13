package in2000.pedalio.data.weather

abstract class WeatherRepository(
    val lat: Double,
    val lon: Double) {
    /**
     * @return Temp in Celsius after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getTemp(timeDelta: Int = 0): Double

    /**
     * @return Percipitation in mm/h after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getPercipitationRate(timeDelta: Int = 0): Double

    /**
     * @return Percipitation in mm after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getPercipitation(timeDelta: Int = 0): Double

    /**
     * @return Relative humidity in % after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getRelativeHumidity(timeDelta: Int = 0): Double

    /**
     * @return Wind direction in degrees after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getWindDirection(timeDelta: Int = 0): Double

    /**
     * @return Wind speed in m/s after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getWindSpeed(timeDelta: Int = 0): Double

    /**
     * @return Wind speed of gusts in m/s after timeDelta minutes
     * @param timeDelta in minutes
     */
   abstract fun getGustSpeed(timeDelta: Int = 0): Double

    /**
     * @return Whether we have radar coverage of the specified location
     */
   abstract fun radarCoverage(): Boolean
}