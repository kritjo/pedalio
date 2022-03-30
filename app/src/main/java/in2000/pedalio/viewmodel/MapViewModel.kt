package in2000.pedalio.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.R
import in2000.pedalio.data.location.LocationRepository
import in2000.pedalio.ui.map.IconBubble
import in2000.pedalio.ui.map.OverlayBubble
import kotlin.math.roundToInt

class MapViewModel(application: Application) : AndroidViewModel(application) {
    val currentPos = MutableLiveData(LatLng())

    // Pair of LatLng and Color
    val polyline = MutableLiveData(Pair(listOf(LatLng()), 0))

    // List of Triple of LatLng, Color, and Opacity
    val polygons = MutableLiveData(listOf<Triple<List<LatLng>, Int, Float>>())

    var overlayBubbles = MutableLiveData(mutableListOf<OverlayBubble>())
    var iconBubbles = MutableLiveData(mutableListOf<IconBubble>())

    val currentLocation =
        LocationRepository(application.applicationContext, LatLng(0.0,0.0))
            .currentPosition

    private var zoomDensityScaler = 3.0f

    // This is to showcase functionality, should rather use domain layer and repositories
    init {
        currentPos.postValue(LatLng(59.91,10.75))

        polyline.postValue(
            Pair(
                listOf(
                    LatLng(59.9475319,10.709509),
                    LatLng(59.9274541,10.8009713),
                    LatLng(59.9022338,10.763559),
                    LatLng(59.9092753,10.6883493),
                    LatLng(59.9475319,10.709509),
                ), Color.RED)
        )

        polygons.postValue(
            listOf(
                Triple(
                    listOf(
                        LatLng(59.9475319,10.709509),
                        LatLng(59.9274541,10.8009713),
                        LatLng(59.9022338,10.763559),
                    ),
                    Color.BLUE,
                    0.3f
                ),
                Triple(
                    listOf(
                        LatLng(59.9022338,10.763559),
                        LatLng(59.9092753,10.6883493),
                        LatLng(59.9475319,10.709509),
                    ),
                    Color.GREEN,
                    0.3f
                )
            )
        )

        val currentBubbles = overlayBubbles.value
        currentBubbles?.addAll(
            listOf(
                OverlayBubble(
                    LatLng(59.9283808, 10.7789658),
                    "Kristian Hjem",
                    Color.DKGRAY,
                    Color.CYAN),
                OverlayBubble(
                    LatLng(59.9138688,10.7522454),
                    "POS",
                    Color.RED,
                    Color.YELLOW)
            ))

        val currentIconBubbles = iconBubbles.value
        currentIconBubbles?.addAll(
            listOf(
                IconBubble(
                    LatLng(59.90, 10.75),
                    R.drawable.ic_launcher_foreground,
                    Color.DKGRAY),
                IconBubble(
                    LatLng(59.93,10.7522454),
                    R.drawable.ic_map,
                    Color.TRANSPARENT)
            ))


    }

    fun getBubbleSquareSize(context: Context): Int {
        // Get screen size
        val density = context.resources.displayMetrics.densityDpi
        return (density / zoomDensityScaler).roundToInt()
    }

    fun updateBubbleZoomLevel(oldZoomLevel: Double, newZoomLevel: Double): Boolean {
        if (oldZoomLevel.toInt() != newZoomLevel.toInt()) {
            zoomDensityScaler = when (newZoomLevel.toInt()) {
                // TODO: Design people decide scale
                in 0..3 -> 10f
                in 4..6 -> 5f
                in 7..10 -> 4f
                else -> 3f
            }
            return true
        }
        return false
    }
}