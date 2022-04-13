package in2000.pedalio.domain.weather

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng

class GetDeviatingWeather(val getWeatherUseCase: GetWeatherUseCase,
                          val deviationLimitTemp: Double,
                          val deviationLimitWind: Double,
                          val deviationLimitPercipation: Double,
                          val possibleDeviationPoints: List<LatLng>,
                          val context: Context) {
    suspend fun deviatingPoints(weatherDataPoint: WeatherDataPoint): MutableList<DeviatingPoint> {
        val localDeviatingPoints = possibleDeviationPoints
            .toMutableList()
            .map { getWeatherUseCase.getWeather(it, context = context) }
            .toMutableList()

        val deviationPoints = mutableListOf<DeviatingPoint>()
        localDeviatingPoints.forEach {
            when {
                notNullDeviation(weatherDataPoint.percipitation, it.percipitation, deviationLimitPercipation) -> {
                    deviationPoints.add(DeviatingPoint(it.pos!!, it, DeviationTypes.PERCIPITATION))
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
    PERCIPITATION
}