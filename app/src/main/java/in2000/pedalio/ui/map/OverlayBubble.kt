package in2000.pedalio.ui.map

import android.widget.Button
import androidx.annotation.ColorInt
import com.tomtom.online.sdk.common.location.LatLng

/**
 * A bubble that is displayed on the map. It is implemented as a button.
 *
 * @property latLng the location of the bubble
 * @property text the text of the bubble
 * @property color the color of the text
 */
class OverlayBubble(
    override var latLng: LatLng,
    var text: String,
    @ColorInt var textColor: Int,
    @ColorInt var backgroundColor: Int
    ) : Bubble {
    override lateinit var button: Button
}

