package in2000.pedalio.data.weather.source.nowcast

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Data Sorce for Nowcast API
 */
class NowcastSource {
    companion object {
        /**
         * Do API call
         * @param endpoint Which Nowcast endpoint to use
         * @param lat Latitude
         * @param lon Longitude
         * @return A nowcast data class
         */
        @JvmStatic
        suspend fun getNowcast(
            endpoint: String,
            lat: Double,
            lon: Double
        ): NowcastCompleteDataClass? {
            return try {
                val (req: Request, _: Response, res: String) =
                    Fuel.get(endpoint, listOf(Pair("lat", lat), Pair("lon", lon)))
                        .awaitStringResponse()
                Json.decodeFromString(res)
            } catch (e: FuelError) {
                Log.e("NowcastSource", "Error: ${e.message}")
                null
            }
        } 
    }
}