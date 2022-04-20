package in2000.pedalio.data.routing.impl

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan
import in2000.pedalio.data.routing.RoutingRepository
import in2000.pedalio.data.routing.source.tomtom.TomtomRoutingSource
import in2000.pedalio.utils.NetworkUtils
import kotlinx.coroutines.runBlocking

class TomtomRoutingRepository(val context: Context) : RoutingRepository {
    /**
     * @see [RoutingRepository.calculateRouteFromWaypoints]
     */
    override fun calculateRouteFromWaypoints(locations: List<LatLng>): RoutePlan? {
        val networkAvailable = runBlocking { return@runBlocking NetworkUtils.isNetworkAvailable() }
        if (!networkAvailable) {
            return null
        }

        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(locations)
        if (route.isSuccess()) return route.value()
        else throw Exception("Error while calculating route: ${route.cause()}")
    }

    /**
     * @see [RoutingRepository.calculateRoute]
     */
    override fun calculateRoute(from: LatLng, to: LatLng): RoutePlan? {
        val networkAvailable = runBlocking { return@runBlocking NetworkUtils.isNetworkAvailable() }
        if (!networkAvailable) return null

        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(listOf(from, to))
        if (route.isSuccess()) return route.value()
        else throw Exception("Error while calculating route: ${route.cause()}")
    }
}