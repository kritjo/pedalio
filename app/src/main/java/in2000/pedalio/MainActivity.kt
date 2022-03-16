package in2000.pedalio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tomtom.online.sdk.common.location.BoundingBox
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        mapView = findViewById<MapView>(R.id.map_fragment);
        mapView.addOnMapReadyCallback { tomtomMap ->
            val osloTopLeft = LatLng(59.92194833346756, 10.718651865762322)
            val osloBottomRight = LatLng(59.903811901433464, 10.764555190843193)
            val boundingBox = BoundingBox(osloTopLeft, osloBottomRight)
            val oslo = LatLng(59.9440703, 10.7189933)
            val focusArea: CameraFocusArea = CameraFocusArea.Builder(boundingBox)
                .pitch(5.0)
                .bearing(MapConstants.ORIENTATION_NORTH.toDouble())
                .build()

            tomtomMap.centerOn(focusArea)
            tomtomMap.addMarker(MarkerBuilder(oslo))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()

    }
}