package in2000.pedalio.ui.map

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tomtom.online.sdk.common.location.BoundingBox
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.*
import in2000.pedalio.R
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

    val bubbleSize by lazy {
        mapViewModel.getBubbleSquareSize(requireContext())
    }

    fun onMapReady(map: TomtomMap) {
        this.tomtomMap = map

        // Draw a line on the map when viewModel says so.
        mapViewModel.polyline.observe(viewLifecycleOwner) {drawPolyline(it.first, it.second)}

        // Draw a polygon on the map when viewModel says so.
        mapViewModel.polygons.observe(viewLifecycleOwner) {it.forEach { polygon ->
            drawPolygon(polygon.first, polygon.second, polygon.third)
        }}

        mapViewModel.overlayBubbles.observe(viewLifecycleOwner) {it.forEach { overlayBubble ->
            initializeOverlayBubble(overlayBubble)
        }
            tomtomMap.addOnCameraChangedListener {
                addOverlayBubbles(tomtomMap.currentBounds)
            }
        }
        mapViewModel.iconBubbles.observe(viewLifecycleOwner) {it.forEach { iconBubble ->
            initializeIconBubble(iconBubble)
        }
            tomtomMap.addOnCameraChangedListener {
                addIconBubbles(tomtomMap.currentBounds)
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
    fun addOverlayBubbles(boundingBox: BoundingBox) {
        val overlay = view?.findViewById<RelativeLayout>(R.id.overlay)

        // This atrocious double loop is needed because of how the .children iterator works.
        // Once a child is removed, the iterator will skip over the following child.
        overlay?.children?.forEach {
            overlay.children.forEach { if (it.tag == "overlayBubble") overlay.removeView(it) }
        }

        mapViewModel.overlayBubbles.value?.forEach {
            if (boundingBox.contains(it.latLng)) {
                val x = tomtomMap.pixelForLatLng(it.latLng).x
                val y = tomtomMap.pixelForLatLng(it.latLng).y
                val params = RelativeLayout.LayoutParams(
                    bubbleSize,
                    bubbleSize
                )
                // Anchor the button to x,y on screen and center it.
                params.leftMargin = x.toInt() - bubbleSize / 2
                params.topMargin = y.toInt() - bubbleSize / 2

                it.button.tag = "overlayBubble"
                overlay?.addView(it.button, params)
            }
        }
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
        val shape = ShapeDrawable(object : OvalShape() {
            override fun draw(canvas: android.graphics.Canvas, paint: android.graphics.Paint) {
                super.draw(canvas, paint)
                paint.color = overlayBubble.backgroundColor
                paint.style = android.graphics.Paint.Style.FILL
                canvas.drawCircle(bubbleSize / 2f, bubbleSize / 2f, bubbleSize / 2f, paint)
            }
        })
        overlayBubble.button.background = shape
    }

    /**
     * Draws/updates the icon bubbles on the map from the list of iconBubbles in the viewModel.
     *
     * @param boundingBox the current bounding box of the map.
     */
    fun addIconBubbles(boundingBox: BoundingBox) {
        val overlay = view?.findViewById<RelativeLayout>(R.id.overlay)

        // This atrocious double loop is needed because of how the .children iterator works.
        // Once a child is removed, the iterator will skip over the following child.
        overlay?.children?.forEach {
            overlay.children.forEach { if (it.tag == "iconBubble") overlay.removeView(it) }
        }

        mapViewModel.iconBubbles.value?.forEach {
            if (boundingBox.contains(it.latLng)) {
                val x = tomtomMap.pixelForLatLng(it.latLng).x
                val y = tomtomMap.pixelForLatLng(it.latLng).y
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                // Anchor the button to x,y on screen and center it.
                params.leftMargin = x.toInt() - bubbleSize / 2
                params.topMargin = y.toInt() - bubbleSize / 2

                it.button.tag = "iconBubble"
                overlay?.addView(it.button, params)
            }
        }
    }

    /**
     * Initializees an icon bubble view. The button parameters are set.
     *
     * @param iconBubble the icon bubble to initialize
     */
    private fun initializeIconBubble(iconBubble: IconBubble) {
        iconBubble.button = ImageButton(this.requireContext())

        val shape = ShapeDrawable(object : OvalShape() {
            override fun draw(canvas: Canvas, paint: Paint) {
                super.draw(canvas, paint)
                canvas.drawCircle(bubbleSize / 2f, bubbleSize / 2f, bubbleSize / 2f, paint)
            }
        })
        iconBubble.button.background = shape

        // round the corners of the image
        iconBubble.button.clipToOutline = true
        // Compat load the drawable icon
        iconBubble.button.setImageDrawable(
            ContextCompat.getDrawable(
                this.requireContext(),
                iconBubble.icon
            )?.apply {
                bounds = Rect(0, 0, bubbleSize, bubbleSize)
            }
        )
        iconBubble.button.scaleType = ImageView.ScaleType.FIT_CENTER

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