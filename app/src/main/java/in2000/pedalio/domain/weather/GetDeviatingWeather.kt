package in2000.pedalio.domain.weather

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng

/**
 * Class used to get the weather difference between a master location and a list of locations.
 *
 * @property getWeatherUseCase
 * @property deviationLimitTemp How many degrees the temperature can deviate from the master
 * location, before it is considered as deviating.
 * @property deviationLimitWind How many meters per second the wind speed can deviate from the
 * master location, before it is considered as deviating.
 * @property deviationLimitPrecipitation How many millimeters per hour the precipitation can deviate
 * from the master location, before it is considered as deviating.
 * @property possibleDeviationPoints The list of locations that should be checked for deviation.
 * @property context
 */
class GetDeviatingWeather(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val deviationLimitTemp: Double,
    private val deviationLimitWind: Double,
    private val deviationLimitPrecipitation: Double,
    private val possibleDeviationPoints: List<LatLng>,
    val context: Context) {

    /**
     * Get the weather difference between the master location and the list of locations.
     *
     * @param weatherDataPoint for master location. @see [GetDeviatingWeather]
     * @return The list of weather differences.
     */
    suspend fun deviatingPoints(weatherDataPoint: WeatherDataPoint): MutableList<DeviatingPoint> {
        // Get the weather for each location.
        val localDeviatingPoints = possibleDeviationPoints
            .toMutableList()
            .map { getWeatherUseCase.getWeather(it, context = context) }
            .toMutableList()

        // Calculate the weather difference.
        val deviationPoints = mutableListOf<DeviatingPoint>()
        localDeviatingPoints.forEach {
            when {
                notNullDeviation(weatherDataPoint.precipitation, it.precipitation, deviationLimitPrecipitation) -> {
                    deviationPoints.add(DeviatingPoint(it.pos!!, it, DeviationTypes.PRECIPITATION))
                }
                notNullDeviation(weatherDataPoint.temperature, it.temperature, deviationLimitTemp) -> {
                    deviationPoints.add(DeviatingPoint(it.pos!!, it, DeviationTypes.TEMPERATURE))
                }
                notNullDeviation(weatherDataPoint.windSpeed, it.windSpeed, deviationLimitWind) -> {
                    deviationPoints.add(DeviatingPoint(it.pos!!, it, DeviationTypes.WIND))
                }
            }
        }
        return deviationPoints
    }

    /**
     * Helper function to check if the difference between two values is greater than the limit.
     *
     * @param v1
     * @param v2
     * @param deviationLimit
     * @return True if the difference is greater than the limit, false otherwise or if one of the
     * values is null.
     */
    private fun notNullDeviation(v1: Double?, v2: Double?, deviationLimit: Double): Boolean {
        return if (v1 != null && v2 != null) {
            v1 - v2 > deviationLimit || v1 - v2 < -deviationLimit
        } else {
            false
        }
    }
}

data class DeviatingPoint(
    val pos: LatLng,
    val weatherDataPoint: WeatherDataPoint,
    val deviation: DeviationTypes // TODO: maybe support more than one deviation type per spot
)

enum class DeviationTypes {
    TEMPERATURE,
    WIND,
    PRECIPITATION
}