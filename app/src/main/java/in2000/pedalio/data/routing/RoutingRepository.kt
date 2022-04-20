package in2000.pedalio.data.routing

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.Route
import com.tomtom.online.sdk.routing.route.RoutePlan

interface RoutingRepository { // Routing API from TomTom
    /**
     * Calculates a route between several points (waypoints).
     */
    fun calculateRouteFromWaypoints(locations : List<LatLng>) : RoutePlan?

    /**
     * Calculates a route between two points.
     */
    fun calculateRoute(from : LatLng, to : LatLng) : RoutePlan?
}