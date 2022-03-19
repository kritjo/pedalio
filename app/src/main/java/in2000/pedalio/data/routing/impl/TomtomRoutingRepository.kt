package in2000.pedalio.data.routing.impl

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.Route
import in2000.pedalio.data.routing.RoutingRepository
import in2000.pedalio.data.routing.source.tomtom.TomtomRoutingSource

class TomtomRoutingRepository : RoutingRepository {
    override fun calculateRoute(locations: List<LatLng>): Route {
        //val source = TomtomRoutingSource()
        TODO("Not yet implemented")
    }
}