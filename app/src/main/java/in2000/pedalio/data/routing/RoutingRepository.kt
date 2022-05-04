package in2000.pedalio.data.routing

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan

/**
 * Specifies the functions required by an AirQuality Repository.
 * Should be implemented by a repository using a specific source of truth.
 */
abstract class RoutingRepository { // Routing API from TomTom
    /**
     * Calculates the route between several points (waypoints)
     * @param locations the points to calculate the route between
     * @return the route plan between the points
     */
    abstract fun calculateRouteFromWaypoints(locations: List<LatLng>): RoutePlan?

    /**
     * Calculates the route between two points
     * @param from the starting point
     * @param to the destination point
     * @return the route plan between the points
     */
    abstract fun calculateRoute(from: LatLng, to: LatLng): RoutePlan?
}