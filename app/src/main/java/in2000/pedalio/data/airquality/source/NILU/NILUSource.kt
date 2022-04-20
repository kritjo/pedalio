package in2000.pedalio.data.airquality.source.NILU

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NILUSource {
    companion object {
        @JvmStatic
        suspend fun getNow(endpoint : String, lat : Double, lon : Double, radius: Int, component : COMPONENTS) : List<NILUDataItem>? {
            // build string
            val url = "$endpoint/$lat/$lon/$radius"
            val parameters = listOf(
                Pair("method", "within"),
                Pair("components", component)
            )
            return try {
                val (_: Request, _: Response, res: String) = Fuel.get(url, parameters)
                    .awaitStringResponse()
                Json.decodeFromString(res)
            } catch (e: FuelError) {
                Log.e("NILUSource", "Error: ${e.message}")
                null
            }
        }
    }

    enum class COMPONENTS(s: String) {
        CO("co"),
        NO("no"),
        NO2("no2"),
        NOx("nox"),
        O3("o3"),
        PM1("pm1"),
        PM10("pm10"),
        PM2_5("pm2.5"),
        SO2("so2"),
        ALL("all") {
            override fun toString(): String {
                return ""
            }
        }
    }
}