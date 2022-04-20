package in2000.pedalio.ui.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.information.FullRoute
import in2000.pedalio.R
import in2000.pedalio.domain.routing.GetRouteAlternativesUseCase

/**
 * An overlay that shows the alternatives of a route.
 * Use [RoutingSelector.newInstance] to get an instance of this overlay.
 */
class RoutingSelector private constructor() : Fragment() {
    private lateinit var shortest: FullRoute
    private lateinit var safest: FullRoute

    // OK to suppress because we have complete control over the potential values (private constructor).
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shortest =
            requireArguments().get(GetRouteAlternativesUseCase.RouteType.SHORTEST.name) as FullRoute
        safest =
            requireArguments().get(GetRouteAlternativesUseCase.RouteType.BIKE.name) as FullRoute
    }

    @SuppressLint("SetTextI18n") // OK as it is only used for international purposes.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_routing_selector, container, false)
        view.findViewById<TextView>(R.id.fastest_route).text = getString(R.string.shortest_route)
        view.findViewById<TextView>(R.id.fastest_km).text =
            "${shortest.summary.lengthInMeters / 1000} km"
        view.findViewById<TextView>(R.id.fastest_min).text =
            "${(shortest.summary.travelTimeInSeconds / 60)} min"
        view.findViewById<Button>(R.id.fastest_button).setOnClickListener {
            // CLICKED TOAST
            Toast.makeText(
                context, getString(R.string.question_fastest_route),
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.fastest_button).setBackgroundColor(SHORTEST_COLOR)
        view.findViewById<Button>(R.id.fastest_button).setTextColor(SHORTEST_CONTRAST_COLOR)
        view.findViewById<CardView>(R.id.fastest_view).setOnClickListener {
            chosenRoute.postValue(shortest.getCoordinates())
        }

        view.findViewById<TextView>(R.id.safest_route).text = getString(R.string.safest_route)
        view.findViewById<TextView>(R.id.safest_km).text =
            "${safest.summary.lengthInMeters / 1000} km"
        view.findViewById<TextView>(R.id.safest_min).text =
            "${safest.summary.travelTimeInSeconds / 60} min"
        view.findViewById<Button>(R.id.safest_button).setOnClickListener {
            // CLICKED TOAST
            Toast.makeText(
                context, getString(R.string.question_safest_route),
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.safest_button).setBackgroundColor(SAFEST_COLOR)
        view.findViewById<Button>(R.id.safest_button).setTextColor(SAFEST_CONTRAST_COLOR)
        view.findViewById<CardView>(R.id.safest_view).setOnClickListener {
            chosenRoute.postValue(safest.getCoordinates())
        }

        return view
    }

    companion object {
        const val SAFEST_COLOR = Color.GREEN
        const val SAFEST_CONTRAST_COLOR = Color.BLACK
        const val SHORTEST_COLOR = Color.YELLOW
        const val SHORTEST_CONTRAST_COLOR = Color.BLACK

        private lateinit var chosenRoute: MutableLiveData<List<LatLng>>

        fun newInstance(
            routes: Map<GetRouteAlternativesUseCase.RouteType, FullRoute>,
            chosenRoute: MutableLiveData<List<LatLng>>
        ) = RoutingSelector().apply {
            arguments = bundleOf(
                Pair(
                    GetRouteAlternativesUseCase.RouteType.SHORTEST.name,
                    routes[GetRouteAlternativesUseCase.RouteType.SHORTEST]
                ),
                Pair(
                    GetRouteAlternativesUseCase.RouteType.BIKE.name,
                    routes[GetRouteAlternativesUseCase.RouteType.BIKE]
                ),
            )
        }.also {
            RoutingSelector.chosenRoute = chosenRoute
        }
    }
}