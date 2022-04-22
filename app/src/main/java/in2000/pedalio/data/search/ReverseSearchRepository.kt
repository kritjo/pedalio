package in2000.pedalio.data.search

import com.tomtom.online.sdk.common.location.LatLng

abstract class ReverseSearchRepository {
    /**
     * @param latLng
     * @return the country name of the given latLng
     */
    abstract suspend fun getCountry(latLng: LatLng): String?
}