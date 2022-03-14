package in2000.pedalio.data.airquality.source.AQF

import com.github.kittinunf.fuel.Fuel
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
        @JvmStatic
        suspend fun getForecast(endpoint : String, lat : Double, lon : Double) : AQFDataClass {
            val parameters = listOf(
                Pair("areaclass", "grunnkrets"),
                Pair("show", "all"),
                Pair("lat", lat.toString()),
                Pair("lon", lon.toString())
            )
            val (req: Request, _: Response, res: String) = Fuel.get(endpoint, parameters).awaitStringResponse()
            return Json.decodeFromString(res)
        }
    }
}