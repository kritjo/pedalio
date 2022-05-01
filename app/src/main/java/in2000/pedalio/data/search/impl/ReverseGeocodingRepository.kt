package in2000.pedalio.data.search.impl

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse
import in2000.pedalio.data.search.ReverseSearchRepository
import in2000.pedalio.utils.NetworkUtils
import io.reactivex.Single
import kotlin.math.floor
import kotlin.math.pow

/**
 * Provides meta-data about a given location using TomTom's API.
 */
class ReverseGeocodingRepository(context: Context) : ReverseSearchRepository() {
    private val api = OnlineSearchApi.create(context, "beN1MD9T81Hr774H5o2lQGGDywkiqcJ8")

    /**
     * @see [ReverseSearchRepository.getCountry]
     */
    override suspend fun getCountry(latLng: LatLng): String? {
        val networkAvailable = NetworkUtils.isNetworkAvailable()
        if (!networkAvailable) return null

        // If the cache contains the country, return it
        // If there is only small changes, return the last position
        // This is possibly dangerous, as you could be on the border of two countries

        val lng = LatLng(latLng.latitude.reduce(4), latLng.longitude.reduce(4))
        if (cache.containsKey(lng)) {
            return cache[lng]!!
        }

        // Otherwise, get the country from the API and cache it
        val query = ReverseGeocoderSearchQueryBuilder
            .create(lng.latitude, lng.longitude)
            .build()
        val res: Single<ReverseGeocoderSearchResponse> = api.reverseGeocoding(query)
        val country = res.blockingGet().addresses.first().address.countryCodeISO3
        cache[lng] = country
        lastPos = lng
        return country
    }

    companion object {
        // Very aggressive caching
        val cache = HashMap<LatLng, String>()
        var lastPos = LatLng(0.0, 0.0)
    }
}

/**
 * This function reduces the number of decimal places in a double
 *
 * @param decp the number of decimal places to reduce to
 * @return the reduced double
 */
private fun Double.reduce(decp: Int): Double {
    var value = this
    value *= 10.0.pow(decp)
    value = floor(value)
    value /= 10.0.pow(decp)
    return value
}