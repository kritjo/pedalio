package in2000.pedalio.data.bikeRoutes

import com.tomtom.online.sdk.common.location.LatLng

/**
 * Specifies the functions required by an Bike Route Repository.
 * Should be implemented by a repository using a specific source of truth.
 */
abstract class BikeRouteRepository {
    /**
     * @return A [List] of routes ([LatLng] lists)
     */
    abstract suspend fun getRoutes(): List<List<LatLng>>
}