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
class TomtomRoutingSource(context: Context) {
    private val travelMode = TravelMode.BICYCLE
    private val apiKey: String = "TeKdz0A0HvMpClYGfpcnnwJlOo9pzcmC"
    private val routingApi: RoutingApi = OnlineRoutingApi.create(context, apiKey)

    /**
     * Creates route specification used in getRouteFromLocations
     */
    private fun createRouteSpecification(
        waypoints: List<LatLng>,
        considerTraffic: Boolean = true
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

    /**
     * Get route from locations
     * @param locations list of locations
     * @param considerTraffic consider traffic
     * @return route
     */
    fun getRouteFromLocations(
        locations: List<LatLng>,
        considerTraffic: Boolean = false
    ): Result<RoutePlan> {
        val routeSpecification = createRouteSpecification(locations, considerTraffic)
        return routingApi.planRoute(routeSpecification)
    }
}