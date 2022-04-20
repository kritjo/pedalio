package in2000.pedalio.data.weather.source.locationforecast

import kotlinx.serialization.Serializable

/**
 * Class to represent Location Forecast API
 */
@Serializable
data class LocationForecastCompleteDataClass(
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
    val air_pressure_at_sea_level: String? = null,
    val air_temperature: String? = null,
    val air_temperature_max: String? = null,
    val air_temperature_min: String? = null,
    val air_temperature_percentile_10: String? = null,
    val air_temperature_percentile_90: String? = null,
    val cloud_area_fraction: String? = null,
    val cloud_area_fraction_high: String? = null,
    val cloud_area_fraction_low: String? = null,
    val cloud_area_fraction_medium: String? = null,
    val dew_point_temperature: String? = null,
    val fog_area_fraction: String? = null,
    val precipitation_amount: String? = null,
    val precipitation_amount_max: String? = null,
    val precipitation_amount_min: String? = null,
    val probability_of_precipitation: String? = null,
    val probability_of_thunder: String? = null,
    val relative_humidity: String? = null,
    val ultraviolet_index_clear_sky: String? = null,
    val wind_from_direction: String? = null,
    val wind_speed: String? = null,
    val wind_speed_of_gust: String? = null,
    val wind_speed_percentile_10: String? = null,
    val wind_speed_percentile_90: String? = null
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
    val details: Details? = null
)

@Serializable
data class Next12Hours(
    val details: Details? = null,
    val summary: Summary? = null
)

@Serializable
data class Next1Hours(
    val details: Details? = null,
    val summary: Summary? = null
)

@Serializable
data class Next6Hours(
    val details: Details? = null,
    val summary: Summary? = null
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
    val symbol_code: String? = null,
    val symbol_confidence: String? = null
)