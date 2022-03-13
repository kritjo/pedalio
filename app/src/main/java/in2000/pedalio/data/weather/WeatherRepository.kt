package in2000.pedalio.data.weather

/**
 * Data repository for access of weather data for a single position. Both current and future data.
 */
abstract class WeatherRepository() {
    /**
     * @return Temp in Celsius after timeDelta minutes
     * @param lat Latitude of position for temp
     * @param lon Longitude of position for temp
     * @param timeDelta in minutes
     */
   abstract fun getTemp(lat: Double,
                        lon: Double,
                        timeDelta: Int = 0): Double?

    /**
     * @return Percipitation in mm/h after timeDelta minutes
     * @param lat Latitude of position for percipitation
     * @param lon Longitude of position for percipitation
     * @param timeDelta in minutes
     */
   abstract fun getPercipitationRate(lat: Double,
                                     lon: Double,
                                     timeDelta: Int = 0): Double?

    /**
     * @return Percipitation in mm after timeDelta minutes
     * @param lat Latitude of position for percipitation
     * @param lon Longitude of position for percipitation
     * @param timeDelta in minutes
     */
   abstract fun getPercipitation(lat: Double,
                                 lon: Double,
                                 timeDelta: Int = 0): Double?

    /**
     * @return Relative humidity in % after timeDelta minutes
     * @param lat Latitude of position for humidity
     * @param lon Longitude of position for humidity
     * @param timeDelta in minutes
     */
   abstract fun getRelativeHumidity(lat: Double,
                                    lon: Double,
                                    timeDelta: Int = 0): Double?

    /**
     * @return Wind direction in degrees after timeDelta minutes
     * @param lat Latitude of position for wind
     * @param lon Longitude of position for wind
     * @param timeDelta in minutes
     */
   abstract fun getWindDirection(lat: Double,
                                 lon: Double,
                                 timeDelta: Int = 0): Double?

    /**
     * @return Wind speed in m/s after timeDelta minutes
     * @param lat Latitude of position for wind
     * @param lon Longitude of position for wind
     * @param timeDelta in minutes
     */
   abstract fun getWindSpeed(lat: Double,
                             lon: Double,
                             timeDelta: Int = 0): Double?

    /**
     * @return Wind speed of gusts in m/s after timeDelta minutes
     * @param lat Latitude of position for wind
     * @param lon Longitude of position for wind
     * @param timeDelta in minutes
     */
   abstract fun getGustSpeed(lat: Double,
                             lon: Double,
                             timeDelta: Int = 0): Double?

    /**
     * @return Whether we have radar coverage of the specified location
     */
   abstract fun radarCoverage(lat: Double,
                              lon: Double): Boolean
}