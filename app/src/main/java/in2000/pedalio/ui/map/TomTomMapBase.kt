package in2000.pedalio.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.location.BasicLocationSource
import com.tomtom.online.sdk.location.LocationSource
import com.tomtom.online.sdk.location.LocationUpdateListener
import com.tomtom.online.sdk.map.*
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SharedPreferences
import in2000.pedalio.viewmodel.MapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [TomTomMapBase.newInstance] factory method to
 * create an instance of this fragment.
 */
class TomTomMapBase : Fragment() {
    private lateinit var tomtomMap: TomtomMap
    private lateinit var mapFragment: MapFragment

    private val mapViewModel: MapViewModel by activityViewModels()
    private val selectorFragment = LayersSelector()

    private var lastPos = LatLng(0.0, 0.0)

    private fun bubbleSize() = mapViewModel.getBubbleSquareSize(requireContext())

    var zoomLevel = 0.0
    var zoomChanged = 0

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val overlayBubbleViews = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    SharedPreferences(requireContext()).askedForGps = true
                    SharedPreferences(requireContext()).gpsToggle = true
                    mapViewModel.permissionCallback()
                    mapViewModel.currentPos.observe(viewLifecycleOwner) { onPosChange(it) }
                } else {
                    SharedPreferences(requireContext()).askedForGps = true
                    SharedPreferences(requireContext()).gpsToggle = false
                }
            }
        initLayerSelector()
    }

    private fun onMapReady(map: TomtomMap) {
        this.tomtomMap = map
        mapViewModel.shouldGetPermission.observe(viewLifecycleOwner){
            if (it) {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        // You can use the API that requires the permission.
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }
                }
                mapViewModel.shouldGetPermission.postValue(false)
            }
        }

        // Draw a line on the map when viewModel says so.
        mapViewModel.polyline.observe(viewLifecycleOwner) {
            removeMapOverlay("polyline")
            it.forEach { line ->
                drawPolyline(line.first, line.second, tag = "polyline") }
        }



        mapViewModel.overlayBubbles.observe(viewLifecycleOwner) { bubbles ->
            bubbles.forEach { overlayBubble ->
                initializeOverlayBubble(overlayBubble) }

            if (SharedPreferences(requireContext()).layerWeather) {
                addBubbles("overlay_bubble", bubbles)
            }

            val cameraChangeListener = TomtomMapCallback.OnCameraChangedListener { cameraPosition ->
                if (mapViewModel.updateBubbleZoomLevel(zoomLevel, cameraPosition.zoom)) zoomChanged = 2
                if (zoomChanged > 0) {
                    bubbles.forEach { initializeOverlayBubble(it) }
                    zoomChanged--
                }
                if (SharedPreferences(requireContext()).layerWeather) {
                    addBubbles("overlay_bubble", bubbles)
                }
            }

            tomtomMap.removeOnCameraChangedListener(cameraChangeListener)
            tomtomMap.addOnCameraChangedListener(cameraChangeListener)
        }

        mapViewModel.bikeRoutes.observe(viewLifecycleOwner) {
            removeMapOverlay("bike_routes")
            if (SharedPreferences(requireContext()).layerBikeRoutes) {
                it.forEach { bikeRoute ->
                    drawPolyline(bikeRoute, Color.BLUE, 3f, "bike_route")
                }
            }
        }

        selectorFragment.requireView().findViewById<Switch>(R.id.switch_weather)
            .setOnCheckedChangeListener { _, checked: Boolean ->
                SharedPreferences(requireContext()).layerWeather = checked
                if (checked) addBubbles(
                    "icon_bubble",
                    mapViewModel.overlayBubbles.value ?: emptyList()
                )
                else {
                    SharedPreferences(requireContext()).layerWeather = false
                    removeBubbles()
                }
        }

        selectorFragment.requireView().findViewById<Switch>(R.id.switch_bikeRoutes)
            .setOnCheckedChangeListener {_, checked: Boolean ->
                SharedPreferences(requireContext()).layerBikeRoutes = checked
                if (checked) {
                    mapViewModel.bikeRoutes.value?.forEach { bikeRoute ->
                        drawPolyline(bikeRoute, Color.BLUE, 3f, "bike_route")
                    }
                }
                else {
                    SharedPreferences(requireContext()).layerBikeRoutes = false
                    removeMapOverlay("bike_route")
                }
            }

        mapViewModel.chosenSearchResult.observe(viewLifecycleOwner) {
            // TODO: Routing to the chosen search result.
            Toast.makeText(requireContext(), it.address?.freeFormAddress, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tomtommapbase, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_tomtom) as MapFragment
        mapViewModel.currentPos.observe(viewLifecycleOwner) {
            onPosChange(it)
        }
        mapFragment.getAsyncMap {
           onMapReady(it)
        }
        view.findViewById<Button>(R.id.layers_button).setOnClickListener{toggleLayerSelector()}
        return view
    }
    private fun initLayerSelector() {
        childFragmentManager.beginTransaction()
            .add(R.id.popup_overlay, selectorFragment).hide(selectorFragment)
            .commitAllowingStateLoss()
    }

    private fun toggleLayerSelector() {
        if (selectorFragment.isVisible){
            childFragmentManager.beginTransaction().hide(selectorFragment).commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction().show(selectorFragment).commitAllowingStateLoss()
        }
    }

    /**
     * onPosChange is called when the current position changes.
     * It updates the map to the new position.
     *
     * @param pos the new position
     */
    private fun onPosChange(pos: LatLng) {
        if (::tomtomMap.isInitialized) {
            // If there is a large change we should recenter the map as it is likely that the user
            // position was not correct previously.
            if (lastPos.latitude - pos.latitude > 1 || lastPos.longitude - pos.longitude > 1
                || lastPos.latitude - pos.latitude < -1 || lastPos.longitude - pos.longitude < -1) {
                val cameraPosition: CameraPosition = CameraPosition.builder()
                    .pitch(5.0)
                    .bearing(MapConstants.ORIENTATION_NORTH.toDouble())
                    .zoom(10.0)
                    .focusPosition(pos)
                    .build()
                tomtomMap.centerOn(cameraPosition)
            }
        } else {
            // If the map is not initialized yet, we should not recenter the map when it is ready.
            mapFragment.getAsyncMap { tomtomMap ->
                val cameraPosition: CameraPosition = CameraPosition.builder()
                    .pitch(5.0)
                    .bearing(MapConstants.ORIENTATION_NORTH.toDouble())
                    .zoom(11.0)
                    .focusPosition(pos)
                    .build()
                tomtomMap.centerOn(cameraPosition)
                tomtomMap.isMyLocationEnabled = true
            }
        }
        lastPos = pos
        mapViewModel.viewModelScope.launch(Dispatchers.IO) {
            mapViewModel.updateWeatherAndDeviations(this@TomTomMapBase.requireContext())
        }
    }

    /**
     * Draws a line on the map using the given coordinates.
     *
     * @param coordinates the coordinates of the line
     * @param color the color of the line
     */
    private fun drawPolyline(coordinates: List<LatLng>, color: Int, width: Float = 3.0f, tag: String) {
        if (coordinates.size < 2) {
            Log.w("drawPolyLine", "Not enough coordinates")
            return
        }
        val polyline = PolylineBuilder.create()
            .coordinates(coordinates)
            .color(color)
            .width(width)
            .tag(tag)
            .build()
        tomtomMap.overlaySettings.addOverlay(polyline)
    }

    private fun removeMapOverlay(tag: String) {
        tomtomMap.overlaySettings.removeOverlayByTag(tag)
    }

    /**
     * Initializes an overlay bubble view. The button parameters are set.
     *
     * @param overlayBubble the overlay bubble to initialize
     */
    private fun initializeOverlayBubble(overlayBubble: OverlayBubble) {
        overlayBubble.button = Button(this.requireContext())
        overlayBubble.button.text = overlayBubble.text
        overlayBubble.button.setTextColor(overlayBubble.textColor)
        val bubbleSize = bubbleSize()
        val shape = ShapeDrawable(object : OvalShape() {
            override fun draw(canvas: Canvas, paint: Paint) {
                super.draw(canvas, paint)
                paint.color = overlayBubble.backgroundColor
                paint.style = Paint.Style.FILL
                canvas.drawCircle(bubbleSize / 2f, bubbleSize / 2f, bubbleSize / 2f, paint)
            }
        })
        overlayBubble.button.background = shape
    }

    /**
     * Draws/updates the bubbles on the map from the list of bubbles in the viewModel.
     *
     * @param tag the tag of the bubble.
     * @param bubbles the list of bubbles to draw.
     */
    private fun addBubbles(tag: String, bubbles: List<Bubble>) {
        val overlay = view?.findViewById<RelativeLayout>(R.id.overlay)
        val bubbleSize = bubbleSize()

        removeBubbles()

        bubbles.forEach {
                val x = tomtomMap.pixelForLatLng(it.latLng).x
                val y = tomtomMap.pixelForLatLng(it.latLng).y
                val params = RelativeLayout.LayoutParams(
                    bubbleSize,
                    bubbleSize
                )
                // Anchor the button to x,y on screen and center it.
                params.leftMargin = x.toInt() - bubbleSize / 2
                params.topMargin = y.toInt() - bubbleSize / 2
                it.button.tag = tag
                overlayBubbleViews.add(it.button)
                overlay?.addView(it.button, params)
        }
    }

    private fun removeBubbles() {
        val overlay = view?.findViewById<RelativeLayout>(R.id.overlay)
        overlayBubbleViews.forEach { overlay?.removeView(it) }
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