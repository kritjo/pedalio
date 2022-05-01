package in2000.pedalio.data.routing.impl

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan
import in2000.pedalio.data.routing.RoutingRepository
import in2000.pedalio.data.routing.source.tomtom.TomtomRoutingSource
import in2000.pedalio.utils.NetworkUtils
import kotlinx.coroutines.runBlocking

/**
 * Implementation of Routing using Tomtom's API.
 */
class TomtomRoutingRepository(val context: Context) : RoutingRepository() {
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
        return if (route.isSuccess()) route.value()
        else null
    }

    /**
     * @see [RoutingRepository.calculateRoute]
     */
    override fun calculateRoute(from: LatLng, to: LatLng): RoutePlan? {
        val networkAvailable = runBlocking { return@runBlocking NetworkUtils.isNetworkAvailable() }
        if (!networkAvailable) return null

        val source = TomtomRoutingSource(context)
        val route = source.getRouteFromLocations(listOf(from, to))
        return if (route.isSuccess()) route.value()
        else null
    }
}