package in2000.pedalio.domain.weather

import com.tomtom.online.sdk.common.location.LatLng

data class WeatherDataPoint(
    val pos: LatLng?,
    val temperature: Double?,
    val percipitation: Double?,
    val humidity: Double?,
    val windSpeed: Double?,
    val windDirection: Double?,
)