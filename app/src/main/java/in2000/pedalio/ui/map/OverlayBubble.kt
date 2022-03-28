package in2000.pedalio.ui.map

import android.widget.Button
import com.tomtom.online.sdk.common.location.LatLng

class OverlayBubble(var latLng: LatLng, var text: String, var color: Int) {
    lateinit var button: Button
}