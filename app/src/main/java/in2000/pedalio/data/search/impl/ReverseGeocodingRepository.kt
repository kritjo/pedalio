package in2000.pedalio.data.search.impl

import android.annotation.SuppressLint
import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse
import in2000.pedalio.data.search.ReverseSearchRepository
import in2000.pedalio.utils.NetworkUtils
import io.reactivex.Single
import kotlinx.coroutines.runBlocking

class ReverseGeocodingRepository(context: Context) : ReverseSearchRepository(){
    private val api = OnlineSearchApi.create(context, "beN1MD9T81Hr774H5o2lQGGDywkiqcJ8")
    override suspend fun getCountry(latLng: LatLng): String? {
        val networkAvailable = NetworkUtils.isNetworkAvailable()
        if (!networkAvailable) {
            return null
        }

        if (cache.containsKey(latLng)) {
            return cache[latLng]!!
        } else if (latLng.latitude - lastPos.latitude < 1 || latLng.longitude - lastPos.longitude < 1
            || latLng.latitude - lastPos.latitude > -1 || latLng.longitude - lastPos.longitude > -1) {
            if (cache.containsKey(lastPos)) {
                return cache[lastPos]!!
            }
        }
        val query = ReverseGeocoderSearchQueryBuilder
            .create(latLng.latitude, latLng.longitude)
            .build()
        val res: Single<ReverseGeocoderSearchResponse> = api.reverseGeocoding(query)
        val country =  res.blockingGet().addresses.first().address.countryCodeISO3
        cache[latLng] = country
        lastPos = latLng
        return country
    }
    companion object {
        // Very aggressive caching
        val cache = HashMap<LatLng, String>()
        var lastPos = LatLng(0.0, 0.0)
    }
}