package in2000.pedalio.data.routing.impl

import android.content.Context
import android.util.Log
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.Route
import com.tomtom.online.sdk.routing.route.RoutePlan
import in2000.pedalio.data.routing.RoutingRepository
import in2000.pedalio.data.routing.source.tomtom.TomtomRoutingSource

class TomtomRoutingRepository(val context : Context) : RoutingRepository {
    override fun calculateRouteFromWaypoints(waypoints: List<LatLng>): RoutePlan {
        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(waypoints)
        if (route.isSuccess()) {
            return route.value()
        } else {
            throw Exception("Error while calculating route: ${route.cause()}")
        }
    }

    override fun calculateRoute(from: LatLng, to: LatLng): RoutePlan {
        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(listOf(from, to))
        if (route.isSuccess()) {
            return route.value()
        } else {
            throw Exception("Error while calculating route: ${route.cause()}")
        }
    }
}