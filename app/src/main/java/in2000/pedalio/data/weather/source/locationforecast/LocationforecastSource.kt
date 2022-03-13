package in2000.pedalio.data.weather.source.locationforecast

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import in2000.pedalio.data.weather.source.LocationForecastCompleteDataClass
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class LocationforecastSource {
    companion object {
        /**
         * Do API call
         * @param endpoint Which Locationforecast endpoint to use
         * @param lat Latitude
         * @param lon Longitude
         * @return A Locationforecast data class
         */
        @JvmStatic
        suspend fun getLocationforecast(
            endpoint: String,
            lat: Double,
            lon: Double
        ): LocationForecastCompleteDataClass {
            val (req: Request, _: Response, res: String) =
                Fuel.get(endpoint, listOf(Pair("lat", lat), Pair("lon", lon)))
                    .awaitStringResponse()
            Log.i("Request", req.toString())
            Log.i("Response", res)
            return Json.decodeFromString(res)
        }
    }
}