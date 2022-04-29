package in2000.pedalio.data.airquality.source.nilu

import android.annotation.SuppressLint
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
        @SuppressLint("LogNotTimber")
        @JvmStatic
        suspend fun getNow(
            endpoint: String,
            lat: Double,
            lon: Double,
            radius: Int,
            component: COMPONENTS
        ): List<NILUDataItem>? {
            // build string
            val url = "$endpoint/$lat/$lon/$radius"
            val parameters = listOf(
                Pair("method", "within"),
                Pair("components", component.getParam)
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

    enum class COMPONENTS(val getParam: String) {
        NO2("no2"),
        PM10("pm10"),
        PM2_5("pm2.5"),
        ALL("") // For all components, we should pass an empty string
    }
}