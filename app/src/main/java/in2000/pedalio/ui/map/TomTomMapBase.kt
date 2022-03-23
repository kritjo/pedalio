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

private const val ARG_LAT = "lat"
private const val ARG_LON = "lon"

/**
 * A simple [Fragment] subclass.
 * Use the [TomTomMapBase.newInstance] factory method to
 * create an instance of this fragment.
 */
class TomTomMapBase : Fragment() {
    lateinit var mapView: MapView

    private val mapViewModel: MapViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tomtommapbase, container, false)
        mapView = view.findViewById(R.id.fragment_tomtom)
        mapViewModel.currentPos.observe(viewLifecycleOwner) { onPosChange(mapView, it) }
        return view
    }

    fun onPosChange(mapView: MapView, pos: LatLng) {
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
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment maptt.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double) {
            val bundle = Bundle()
            bundle.putString(ARG_LAT, "59.91")
            bundle.putString(ARG_LON, "10.75")
            val ttmb = TomTomMapBase()
            ttmb.arguments = bundle
        }
    }
}