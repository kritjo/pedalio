package in2000.pedalio.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tomtom.online.sdk.common.location.LatLng

class MapViewModel(application: Application) : AndroidViewModel(application) {
    val currentPos = MutableLiveData(LatLng())

    // Pair of LatLng and Color
    val polyline = MutableLiveData(Pair(listOf(LatLng()), 0))

    // List of Triple of LatLng, Color, and Opacity
    val polygons = MutableLiveData(listOf<Triple<List<LatLng>, Int, Float>>())

    init {
        val sharedPreferences = application.getSharedPreferences("user_pos", MODE_PRIVATE)
        if (!sharedPreferences.contains("is_enabled")) {
            // TODO: Actually request permissions, should maybe use domain layer for this
            // ...
            // with (sharedPreferences.edit()) {
            //    putBoolean("is_enabled", true)
            //    apply()
            //}
        }
        if (sharedPreferences.getBoolean("is_enabled", false)) {
            // TODO: Get updated user location from data repository
        } else {
            currentPos.postValue(LatLng(59.91,10.75))
        }

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
    }
}