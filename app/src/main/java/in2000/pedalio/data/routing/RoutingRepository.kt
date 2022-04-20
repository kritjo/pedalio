package in2000.pedalio.data.routing

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan

interface RoutingRepository { // Routing API from TomTom
    /**
     * Calculates the route between several points (waypoints)
     * @param locations the points to calculate the route between
     * @return the route plan between the points
     */
    fun calculateRouteFromWaypoints(locations : List<LatLng>) : RoutePlan?

    /**
     * Calculates the route between two points
     * @param from the starting point
     * @param to the destination point
     * @return the route plan between the points
     */
    fun calculateRoute(from : LatLng, to : LatLng) : RoutePlan?
}