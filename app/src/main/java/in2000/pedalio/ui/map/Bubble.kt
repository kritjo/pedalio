package in2000.pedalio.ui.map

import android.view.View
import com.tomtom.online.sdk.common.location.LatLng

interface Bubble {
    val latLng: LatLng
    val button: View
}