package in2000.pedalio.data.airquality.source.nilu

import kotlinx.serialization.Serializable

@Serializable
data class NILUDataItem(
    val area: String? = null,
    val color: String? = null,
    val component: String? = null,
    val eoi: String? = null,
    val fromTime: String? = null,
    val id: Int? = null,
    val index: Int? = null,
    val isValid: Boolean? = null,
    val isVisible: Boolean? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val municipality: String? = null,
    val station: String? = null,
    val timestep: Int? = null,
    val toTime: String? = null,
    val type: String? = null,
    val unit: String? = null,
    val value: Double? = null,
    val zone: String? = null
)