package in2000.pedalio.data.bikeRoutes

import com.tomtom.online.sdk.common.location.LatLng

abstract class BikeRouteRepository {
    /**
     * @return A [List] of routes ([LatLng] lists)
     */
    abstract suspend fun getRoutes(): List<List<LatLng>>
}