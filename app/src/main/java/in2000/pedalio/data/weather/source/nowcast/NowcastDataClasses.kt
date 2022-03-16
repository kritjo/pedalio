package in2000.pedalio.data.weather.source.nowcast

import kotlinx.serialization.Serializable

/**
 * Class to represent Nowcast API response
 */
@Serializable
data class NowcastCompleteDataClass(
    val geometry: Geometry? = null,
    val properties: Properties? = null,
    val type: String? = null
)

@Serializable
data class Geometry(
    val coordinates: List<Double>? = null,
    val type: String? = null
)

@Serializable
data class Properties(
    val meta: Meta? = null,
    val timeseries: List<Timesery>? = null
)

@Serializable
data class Meta(
    val radar_coverage: String? = null,
    val units: Units? = null,
    val updated_at: String? = null
)

@Serializable
data class Timesery(
    val `data`: Data? = null,
    val time: String? = null
)

@Serializable
data class Units(
    val air_temperature: String? = null,
    val precipitation_amount: String? = null,
    val precipitation_rate: String? = null,
    val relative_humidity: String? = null,
    val wind_from_direction: String? = null,
    val wind_speed: String? = null,
    val wind_speed_of_gust: String? = null
)

@Serializable
data class Data(
    val instant: Instant? = null,
    val next_1_hours: Next1Hours? = null
)

@Serializable
data class Instant(
    val details: Details? = null
)

@Serializable
data class Next1Hours(
    val details: Details? = null,
    val summary: Summary? = null
)

@Serializable
data class Details(
    val air_temperature: Double? = null,
    val precipitation_amount: Double? = null,
    val precipitation_rate: Double? = null,
    val relative_humidity: Double? = null,
    val wind_from_direction: Double? = null,
    val wind_speed: Double? = null,
    val wind_speed_of_gust: Double? = null
)

@Serializable
data class Summary(
    val symbol_code: String? = null
)