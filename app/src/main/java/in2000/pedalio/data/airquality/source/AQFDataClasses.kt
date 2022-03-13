package in2000.pedalio.data.airquality.source

data class AQFDataClass(
    val data: Data = Data(),
    val meta: Meta = Meta()
)

data class Data(
    val time: List<Time> = listOf()
)

data class Meta(
    val location: Location = Location(),
    val reftime: String = "",
    val sublocations: List<Any> = listOf(),
    val superlocation: Superlocation = Superlocation()
)

data class Time(
    val from: String = "",
    val reason: Reason = Reason(),
    val to: String = "",
    val variables: Variables = Variables()
)

data class Reason(
    val sources: List<String> = listOf(),
    val variables: List<String> = listOf()
)

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

data class Measurement(
    val units: String = "",
    val value: Double = 0.0
)

data class Location(
    val areaclass: String = "",
    val areacode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val name: String = "",
    val path: String = "",
    val superareacode: String = ""
)

data class Superlocation(
    val areaclass: String = "",
    val areacode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val name: String = "",
    val path: String = "",
    val superareacode: String = ""
)