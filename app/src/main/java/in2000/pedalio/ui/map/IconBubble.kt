package in2000.pedalio.ui.map

import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.tomtom.online.sdk.common.location.LatLng

class IconBubble(override var latLng: LatLng, @DrawableRes var icon: Int, @ColorInt var color: Int) : Bubble {
    override lateinit var button: ImageButton
}