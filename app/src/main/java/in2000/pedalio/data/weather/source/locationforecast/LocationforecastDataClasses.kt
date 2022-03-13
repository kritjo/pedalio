package in2000.pedalio.data.weather.source

import kotlinx.serialization.Serializable

/**
 * Class to represent Location Forecast API
 */
@Serializable
data class LocationForecastCompleteDataClass(
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
    val air_pressure_at_sea_level: String,
    val air_temperature: String,
    val air_temperature_max: String,
    val air_temperature_min: String,
    val air_temperature_percentile_10: String,
    val air_temperature_percentile_90: String,
    val cloud_area_fraction: String,
    val cloud_area_fraction_high: String,
    val cloud_area_fraction_low: String,
    val cloud_area_fraction_medium: String,
    val dew_point_temperature: String,
    val fog_area_fraction: String,
    val precipitation_amount: String,
    val precipitation_amount_max: String,
    val precipitation_amount_min: String,
    val probability_of_precipitation: String,
    val probability_of_thunder: String,
    val relative_humidity: String,
    val ultraviolet_index_clear_sky: String,
    val wind_from_direction: String,
    val wind_speed: String,
    val wind_speed_of_gust: String,
    val wind_speed_percentile_10: String,
    val wind_speed_percentile_90: String
)

@Serializable
data class Data(
    val instant: Instant,
    val next_12_hours: Next12Hours? = null,
    val next_1_hours: Next1Hours? = null,
    val next_6_hours: Next6Hours? = null
)

@Serializable
data class Instant(
    val details: Details
)

@Serializable
data class Next12Hours(
    val details: Details,
    val summary: Summary
)

@Serializable
data class Next1Hours(
    val details: Details,
    val summary: Summary
)

@Serializable
data class Next6Hours(
    val details: Details,
    val summary: Summary
)

@Serializable
data class Details(
    val air_pressure_at_sea_level: Double? = null,
    val air_temperature: Double? = null,
    val air_temperature_percentile_10: Double? = null,
    val air_temperature_percentile_90: Double? = null,
    val cloud_area_fraction: Double? = null,
    val cloud_area_fraction_high: Double? = null,
    val cloud_area_fraction_low: Double? = null,
    val cloud_area_fraction_medium: Double? = null,
    val dew_point_temperature: Double? = null,
    val fog_area_fraction: Double? = null,
    val relative_humidity: Double? = null,
    val ultraviolet_index_clear_sky: Double? = null,
    val wind_from_direction: Double? = null,
    val wind_speed: Double? = null,
    val wind_speed_of_gust: Double? = null,
    val wind_speed_percentile_10: Double? = null,
    val wind_speed_percentile_90: Double? = null,
    val air_temperature_max: Double? = null,
    val air_temperature_min: Double? = null,
    val precipitation_amount: Double? = null,
    val precipitation_amount_max: Double? = null,
    val precipitation_amount_min: Double? = null,
    val probability_of_precipitation: Double? = null,
    val probability_of_thunder: Double? = null,
)

@Serializable
data class Summary(
    val symbol_code: String,
    val symbol_confidence: String
)