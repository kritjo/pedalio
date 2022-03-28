package in2000.pedalio.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.activityViewModels
import com.tomtom.online.sdk.common.location.BoundingBox
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

        // Draw a line on the map when viewModel says so.
        mapViewModel.polyline.observe(viewLifecycleOwner) {drawPolyline(it.first, it.second)}

        // Draw a polygon on the map when viewModel says so.
        mapViewModel.polygons.observe(viewLifecycleOwner) {it.forEach { polygon ->
            drawPolygon(polygon.first, polygon.second, polygon.third)
        }}

        mapViewModel.overlayBubbles.observe(viewLifecycleOwner) {it.forEach { overlayBubble ->
            overlayBubble.button = Button(this.requireContext())
            overlayBubble.button.text = overlayBubble.text
            overlayBubble.button.setTextColor(overlayBubble.color)
            overlayBubble.button.setBackgroundResource(R.drawable.overlay_bubble)
            overlayBubble.button.setPadding(30, 10, 30, 10)
        }
            tomtomMap.addOnCameraChangedListener {
                addBubbles(tomtomMap.currentBounds)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tomtommapbase, container, false)
        mapView = view.findViewById(R.id.fragment_tomtom)
        mapViewModel.currentPos.observe(viewLifecycleOwner) { onPosChange(it) }
        mapView.addOnMapReadyCallback { onMapReady(it) }
        return view
    }

    /**
     * onPosChange is called when the current position changes.
     * It updates the map to the new position.
     *
     * @param pos the new position
     */
    private fun onPosChange(pos: LatLng) {
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

    /**
     * Draws a line on the map using the given coordinates.
     *
     * @param coordinates the coordinates of the line
     * @param color the color of the line
     */
    fun drawPolyline(coordinates: List<LatLng>, color: Int) {
        val polyline = PolylineBuilder.create()
            .coordinates(coordinates)
            .color(color)
            .build()
        tomtomMap.overlaySettings.addOverlay(polyline)
    }

    /**
     * Draw a polygon on the map.
     *
     * @param coordinates The coordinates at the vertices of the polygon.
     * @param color The fill color of the polygon.
     * @param opacity The opacity of the polygon.
     */
    fun drawPolygon(coordinates: List<LatLng>, color: Int, opacity: Float) {
        val polygon = PolygonBuilder.create()
            .coordinates(coordinates)
            .color(color)
            .opacity(opacity)
            .build()
        tomtomMap.overlaySettings.addOverlay(polygon)
    }

    /**
     * Draws/updates the bubbles on the map from the list of overlayBubbles in the viewModel.
     *
     * @param boundingBox the current bounding box of the map.
     */
    fun addBubbles(boundingBox: BoundingBox) {
        val overlay = view?.findViewById<RelativeLayout>(R.id.overlay)
        overlay?.removeAllViews()
        mapViewModel.overlayBubbles.value?.forEach {
            if (boundingBox.contains(it.latLng)) {
                val x = tomtomMap.pixelForLatLng(it.latLng).x
                val y = tomtomMap.pixelForLatLng(it.latLng).y
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                // Anchor the button to x,y on screen
                params.leftMargin = x.toInt()
                params.topMargin = y.toInt()

                overlay?.addView(it.button, params)
            }
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