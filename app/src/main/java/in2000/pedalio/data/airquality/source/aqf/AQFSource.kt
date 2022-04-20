package in2000.pedalio.data.airquality.source.aqf

import android.annotation.SuppressLint
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Air Quality Forecast source from met api (weatherapi)
 *
 */
class AQFSource {
    companion object {
        @SuppressLint("LogNotTimber")
        @JvmStatic
        suspend fun getForecast(endpoint : String, lat : Double, lon : Double) : AQFDataClass? {
            val parameters = listOf(
                Pair("areaclass", "grunnkrets"),
                Pair("show", "all"),
                Pair("lat", lat.toString()),
                Pair("lon", lon.toString())
            )
            return try {
                val (_: Request, _: Response, res: String) = Fuel.get(endpoint, parameters)
                    .awaitStringResponse()
                Json.decodeFromString(res)
            } catch (e: FuelError) {
                Log.e("AQFSource", "Error: ${e.message}")
                null
            }

        }
    }
}