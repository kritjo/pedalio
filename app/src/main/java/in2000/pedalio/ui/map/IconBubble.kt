package in2000.pedalio.ui.map

import android.widget.ImageButton
import androidx.annotation.DrawableRes
import com.tomtom.online.sdk.common.location.LatLng

class IconBubble(var latLng: LatLng, @DrawableRes var icon: Int) {
    lateinit var button: ImageButton
}