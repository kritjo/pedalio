package in2000.pedalio.data.weather.source

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NowcastSource {
    companion object {
        @JvmStatic
        suspend fun getNowcast(
            endpoint: String,
            lat: Double,
            lon: Double
        ): NowcastCompleteDataClass {
            val (req: Request, _: Response, res: String) =
                Fuel.get(endpoint, listOf(Pair("lat", lat), Pair("lon", lon)))
                    .awaitStringResponse()
            Log.i("Request", req.toString())
            Log.i("Response", res)
            return Json.decodeFromString(res)
        } 
    }
}