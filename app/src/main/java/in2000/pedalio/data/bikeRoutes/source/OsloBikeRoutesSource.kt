package in2000.pedalio.data.bikeRoutes.source

import android.annotation.SuppressLint
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.coroutines.awaitStringResponse

/**
 * Source for Oslo Kommune's Bike route API.
 */
class OsloBikeRoutesSource {
    companion object {
        /**
         * @param endpoint The endpoint from Oslo Kommune.
         * @return String value of GEOJson.
         */
        @SuppressLint("LogNotTimber")
        @JvmStatic
        suspend fun getRoutes(
            endpoint: String
        ): String? {
            // Third is the result
            return try {
                Fuel.get(endpoint).awaitStringResponse().third
            } catch (e: FuelError) {
                Log.e("OsloBikeRoutesSource", "Error: ${e.message}")
                null
            }
        }
    }
}