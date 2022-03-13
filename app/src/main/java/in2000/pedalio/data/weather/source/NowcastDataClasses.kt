package in2000.pedalio.data.weather.source

import kotlinx.serialization.Serializable

@Serializable
data class NowcastCompleteDataClass(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)

@Serializable
data class Geometry(
    val coordinates: List<Double>,
    val type: String
)

@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<Timesery>
)

@Serializable
data class Meta(
    val radar_coverage: String,
    val units: Units,
    val updated_at: String
)

@Serializable
data class Timesery(
    val `data`: Data,
    val time: String
)

@Serializable
data class Units(
    val air_temperature: String,
    val precipitation_amount: String,
    val precipitation_rate: String,
    val relative_humidity: String,
    val wind_from_direction: String,
    val wind_speed: String,
    val wind_speed_of_gust: String
)

@Serializable
data class Data(
    val instant: Instant,
    val next_1_hours: Next1Hours? = null
)

@Serializable
data class Instant(
    val details: Details
)

@Serializable
data class Next1Hours(
    val details: DetailsX,
    val summary: Summary
)

@Serializable
data class Details(
    val air_temperature: Double? = null,
    val precipitation_rate: Double? = null,
    val relative_humidity: Double? = null,
    val wind_from_direction: Double? = null,
    val wind_speed: Double? = null,
    val wind_speed_of_gust: Double? = null
)

@Serializable
data class DetailsX(
    val precipitation_amount: Double
)

@Serializable
data class Summary(
    val symbol_code: String
)