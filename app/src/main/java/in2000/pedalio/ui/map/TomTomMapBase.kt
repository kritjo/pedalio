package in2000.pedalio.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import in2000.pedalio.R

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.*
import com.tomtom.online.sdk.map.MapView
import in2000.pedalio.viewmodel.MapViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TomTomMapBase.newInstance] factory method to
 * create an instance of this fragment.
 */
class TomTomMapBase : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var tomtomMap: TomtomMap

    private val mapViewModel: MapViewModel by activityViewModels()

    fun onMapReady(map: TomtomMap) {
        this.tomtomMap = map
        mapViewModel.polyline.observe(viewLifecycleOwner) {drawPolyline(it.first, it.second)}
        mapViewModel.polygons.observe(viewLifecycleOwner) {it.forEach { polygon ->
            drawPolygon(polygon.first, polygon.second, polygon.third)
        }}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tomtommapbase, container, false)
        mapView = view.findViewById(R.id.fragment_tomtom)
        mapViewModel.currentPos.observe(viewLifecycleOwner) { onPosChange(mapView, it) }
        mapView.addOnMapReadyCallback { onMapReady(it) }
        return view
    }

    private fun onPosChange(mapView: MapView, pos: LatLng) {
        mapView.addOnMapReadyCallback { tomtomMap ->
            val cameraPosition: CameraPosition = CameraPosition.builder()
                .pitch(5.0)
                .bearing(MapConstants.ORIENTATION_NORTH.toDouble())
                .zoom(13.0)
                .focusPosition(pos)
                .build()
            tomtomMap.centerOn(cameraPosition)
            tomtomMap.isMyLocationEnabled = true
        }
    }

    // Draw a polyline on the map
    fun drawPolyline(points: List<LatLng>, color: Int) {
        val polyline = PolylineBuilder.create()
            .coordinates(points)
            .color(color)
            .build()
        tomtomMap.overlaySettings.addOverlay(polyline)
    }

    // Draw a polygon on the map
    fun drawPolygon(points: List<LatLng>, color: Int, opacity: Float) {
        val polygon = PolygonBuilder.create()
            .coordinates(points)
            .color(color)
            .opacity(opacity)
            .build()
        tomtomMap.overlaySettings.addOverlay(polygon)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * @return A new instance of fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = TomTomMapBase()
    }
}