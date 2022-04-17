package in2000.pedalio.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
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
import com.tomtom.online.sdk.map.*
import com.tomtom.online.sdk.map.route.RouteLayerStyle
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SharedPreferences
import in2000.pedalio.domain.routing.GetRouteAlternativesUseCase
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
    private lateinit var routingSelectorFragment: RoutingSelector

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
                } else {
                    SharedPreferences(requireContext()).askedForGps = true
                    SharedPreferences(requireContext()).gpsToggle = false
                }
            }
        initLayerSelector()
    }

    private fun onMapReady(map: TomtomMap) {
        tomtomMap = map
        // This callback should be first thing after this.tomtomMap assign
        mapViewModel.registerListener = tomtomMap::addLocationUpdateListener
        mapViewModel.currentPos().observe(viewLifecycleOwner) {
            onPosChange(it)
        }

        tomtomMap.isMyLocationEnabled = true

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



        var currentBubblesCameraChangeListener: TomtomMapCallback.OnCameraChangedListener? = null
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

            if (currentBubblesCameraChangeListener != null) {
                tomtomMap.removeOnCameraChangedListener(currentBubblesCameraChangeListener!!)
            }
            currentBubblesCameraChangeListener = cameraChangeListener
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

        mapViewModel.chosenSearchResult.observe(viewLifecycleOwner) { searchResult ->
            if (searchResult == null) return@observe
            if (::routingSelectorFragment.isInitialized)
            childFragmentManager.beginTransaction()
                .remove(routingSelectorFragment)
                .commitAllowingStateLoss()

            val from = mapViewModel.currentPos().value!!
            val to = searchResult.position
            mapViewModel.viewModelScope.launch(Dispatchers.IO) {
                val routes = GetRouteAlternativesUseCase.getRouteAlternatives(from, to, requireContext())
                routes.forEach { route ->
                    when (route.key) {
                        GetRouteAlternativesUseCase.RouteType.BIKE -> {
                            val rb = RouteBuilder(route.value.getCoordinates())
                            val rsb = RouteStyleBuilder.create()
                            rsb.withFillColor(RoutingSelector.SAFEST_COLOR)
                            rb.style(rsb.build())
                            tomtomMap.addRoute(rb)
                            mapViewModel.routesOnDisplay.add(rb.id)
                        }
                        GetRouteAlternativesUseCase.RouteType.SHORTEST -> {
                            val rb = RouteBuilder(route.value.getCoordinates())
                            val rsb = RouteStyleBuilder.create()
                            rsb.withFillColor(RoutingSelector.SHORTEST_COLOR)
                            rb.style(rsb.build())
                            tomtomMap.addRoute(rb)
                            mapViewModel.routesOnDisplay.add(rb.id)
                        }
                    }
                }
                routingSelectorFragment = RoutingSelector.newInstance(routes, mapViewModel.chosenRoute)
                mapViewModel.chosenSearchResult.postValue(null)
                childFragmentManager.beginTransaction()
                    .add(R.id.routing_overlay, routingSelectorFragment)
                    .commitAllowingStateLoss()
            }
        }

        mapViewModel.chosenRoute.observe(viewLifecycleOwner) { list ->
            mapViewModel.routesOnDisplay.forEach {
                tomtomMap.removeRoute(it)
            }
            childFragmentManager.beginTransaction()
                .hide(routingSelectorFragment)
                .commitAllowingStateLoss()
            val rb = RouteBuilder(list)
            tomtomMap.addRoute(rb)
            tomtomMap.centerOn(CameraPosition.builder()
                .pitch(20.0)
                .zoom(20.0)
                .focusPosition(mapViewModel.currentPos().value!!)
                .build()
            )
            tomtomMap.activateProgressAlongRoute(rb.id, RouteLayerStyle.Builder().build())
            mapViewModel.currentPos().observe(viewLifecycleOwner) {
                tomtomMap.updateProgressAlongRoute(rb.id, it.toLocation())
                tomtomMap.centerOn(CameraPosition.builder()
                    .pitch(20.0)
                    .zoom(20.0)
                    .focusPosition(it)
                    .build()
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tomtommapbase, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_tomtom) as MapFragment
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
                    .zoom(11.0)
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