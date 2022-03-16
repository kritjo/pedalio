package in2000.pedalio.data.airquality.source.NILU

import kotlinx.serialization.Serializable

@Serializable
data class NILUDataItem(
    val area: String = "",
    val color: String = "",
    val component: String = "",
    val eoi: String = "",
    val fromTime: String = "",
    val id: Int = 0,
    val index: Int = 0,
    val isValid: Boolean = false,
    val isVisible: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val municipality: String = "",
    val station: String = "",
    val timestep: Int = 0,
    val toTime: String = "",
    val type: String = "",
    val unit: String = "",
    val value: Double = 0.0,
    val zone: String = ""
)