package in2000.pedalio.data.routing.impl

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan
import in2000.pedalio.data.routing.RoutingRepository
import in2000.pedalio.data.routing.source.tomtom.TomtomRoutingSource
import in2000.pedalio.utils.NetworkUtils
import kotlinx.coroutines.runBlocking

class TomtomRoutingRepository(val context : Context) : RoutingRepository {
    /**
     * Calculates the route between several points (waypoints)
     * @param locations the points to calculate the route between
     * @return the route plan between the points
     */
    override fun calculateRouteFromWaypoints(locations: List<LatLng>): RoutePlan? {
        val networkAvailable = runBlocking { return@runBlocking NetworkUtils.isNetworkAvailable() }
        if (!networkAvailable) {
            return null
        }
        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(locations)
        if (route.isSuccess()) {
            return route.value()
        } else {
            throw Exception("Error while calculating route: ${route.cause()}")
        }
    }

    /**
     * Calculates the route between two points
     * @param from the starting point
     * @param to the destination point
     * @return the route plan between the points
     */
    override fun calculateRoute(from: LatLng, to: LatLng): RoutePlan? {
        val networkAvailable = runBlocking { return@runBlocking NetworkUtils.isNetworkAvailable() }
        if (!networkAvailable) {
            return null
        }

        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(listOf(from, to))
        if (route.isSuccess()) {
            return route.value()
        } else {
            throw Exception("Error while calculating route: ${route.cause()}")
        }
    }
}