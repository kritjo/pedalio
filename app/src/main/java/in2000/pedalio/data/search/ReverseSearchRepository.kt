package in2000.pedalio.data.search

import com.tomtom.online.sdk.common.location.LatLng

abstract class ReverseSearchRepository {
    abstract suspend fun getCountry(latLng: LatLng): String
}