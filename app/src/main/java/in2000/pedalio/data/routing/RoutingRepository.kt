package in2000.pedalio.data.routing

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.Route

interface RoutingRepository { // Routing API from TomTom
    fun calculateRoute(locations : List<LatLng>) : Route
}