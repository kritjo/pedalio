package in2000.pedalio.data.airquality.source.AQF

import kotlinx.serialization.Serializable

@Serializable
data class AQFDataClass(
    val data: Data = Data(),
    val meta: Meta = Meta()
)

@Serializable
data class Data(
    val time: List<Time> = listOf()
)

@Serializable
data class Meta(
    val location: Location = Location(),
    val reftime: String = "",
    val sublocations: List<Location> = listOf(),
    val superlocation: Superlocation = Superlocation()
)

@Serializable
data class Time(
    val from: String = "",
    val reason: Reason = Reason(),
    val to: String = "",
    val variables: Variables = Variables()
)

@Serializable
data class Reason(
    val sources: List<String> = listOf(),
    val variables: List<String> = listOf()
)

@Serializable
data class Variables(
    val AQI: Measurement = Measurement(),
    val AQI_no2: Measurement = Measurement(),
    val AQI_o3: Measurement = Measurement(),
    val AQI_pm10: Measurement = Measurement(),
    val AQI_pm25: Measurement = Measurement(),
    val no2_concentration: Measurement = Measurement(),
    val no2_local_fraction_heating: Measurement = Measurement(),
    val no2_local_fraction_industry: Measurement = Measurement(),
    val no2_local_fraction_shipping: Measurement = Measurement(),
    val no2_local_fraction_traffic_exhaust: Measurement = Measurement(),
    val no2_local_fraction_traffic_nonexhaust: Measurement = Measurement(),
    val no2_nonlocal_fraction: Measurement = Measurement(),
    val no2_nonlocal_fraction_seasalt: Measurement = Measurement(),
    val o3_concentration: Measurement = Measurement(),
    val o3_local_fraction_heating: Measurement = Measurement(),
    val o3_local_fraction_industry: Measurement = Measurement(),
    val o3_local_fraction_shipping: Measurement = Measurement(),
    val o3_local_fraction_traffic_exhaust: Measurement = Measurement(),
    val o3_local_fraction_traffic_nonexhaust: Measurement = Measurement(),
    val o3_nonlocal_fraction: Measurement = Measurement(),
    val o3_nonlocal_fraction_seasalt: Measurement = Measurement(),
    val pm10_concentration: Measurement = Measurement(),
    val pm10_local_fraction_heating: Measurement = Measurement(),
    val pm10_local_fraction_industry: Measurement = Measurement(),
    val pm10_local_fraction_shipping: Measurement = Measurement(),
    val pm10_local_fraction_traffic_exhaust: Measurement = Measurement(),
    val pm10_local_fraction_traffic_nonexhaust: Measurement = Measurement(),
    val pm10_nonlocal_fraction: Measurement = Measurement(),
    val pm10_nonlocal_fraction_seasalt: Measurement = Measurement(),
    val pm25_concentration: Measurement = Measurement(),
    val pm25_local_fraction_heating: Measurement = Measurement(),
    val pm25_local_fraction_industry: Measurement = Measurement(),
    val pm25_local_fraction_shipping: Measurement = Measurement(),
    val pm25_local_fraction_traffic_exhaust: Measurement = Measurement(),
    val pm25_local_fraction_traffic_nonexhaust: Measurement = Measurement(),
    val pm25_nonlocal_fraction: Measurement = Measurement(),
    val pm25_nonlocal_fraction_seasalt: Measurement = Measurement()
)

@Serializable
data class Measurement(
    val units: String = "",
    val value: Double = 0.0
)

@Serializable
data class Location(
    val areaclass: String = "",
    val areacode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val name: String = "",
    val path: String = "",
    val superareacode: String = ""
)

@Serializable
data class Superlocation(
    val areaclass: String = "",
    val areacode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val name: String = "",
    val path: String = "",
    val superareacode: String = ""
)