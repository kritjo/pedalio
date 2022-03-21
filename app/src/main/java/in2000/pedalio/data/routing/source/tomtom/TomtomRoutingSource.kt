package in2000.pedalio.data.routing.source.tomtom

import android.content.Context
import com.tomtom.online.sdk.common.Result
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.OnlineRoutingApi
import com.tomtom.online.sdk.routing.RoutingApi
import com.tomtom.online.sdk.routing.route.RouteCalculationDescriptor
import com.tomtom.online.sdk.routing.route.RouteDescriptor
import com.tomtom.online.sdk.routing.route.RoutePlan
import com.tomtom.online.sdk.routing.route.RouteSpecification
import com.tomtom.online.sdk.routing.route.description.TravelMode

/**
 * Data source for routing with TomTom.
 * Doc: https://developer.tomtom.com/maps-android-sdk/documentation/routing/documentation
 */
class TomtomRoutingSource (context : Context) {
    private val travelMode = TravelMode.BICYCLE
    private val apiKey: String = "beN1MD9T81Hr774H5o2lQGGDywkiqcJ8"
    private val routingApi: RoutingApi = OnlineRoutingApi.create(context, apiKey);

    private fun createRouteSpecification(
        waypoints: List<LatLng>,
        considerTraffic: Boolean = false
    ): RouteSpecification {
        val routeDescriptor = RouteDescriptor.Builder()
            .travelMode(travelMode)
            .considerTraffic(considerTraffic)
            .build()
        val routeCalculationDescriptor = RouteCalculationDescriptor.Builder()
            .routeDescription(routeDescriptor)
            .waypoints(waypoints)
            .build()
        // Create route plan, start of list is origin, end of list is destination.
        return RouteSpecification.Builder(waypoints[0], waypoints[waypoints.size - 1])
            .routeCalculationDescriptor(routeCalculationDescriptor)
            .build()
    }

    fun getRouteFromLocations(locations: List<LatLng>, origin : LatLng? = null, destination: LatLng? = null, considerTraffic: Boolean = false): Result<RoutePlan> {
        val routeSpecification = createRouteSpecification(locations, considerTraffic)
        return routingApi.planRoute(routeSpecification)
    }
}