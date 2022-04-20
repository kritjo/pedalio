package in2000.pedalio.ui.map

import android.view.View
import com.tomtom.online.sdk.common.location.LatLng
/**
 * A bubble that can be displayed on the map.
 * @param latLng the location of the bubble
 */
interface Bubble {
    val latLng: LatLng
    val button: View
}