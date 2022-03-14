package in2000.pedalio.data.airquality.source.NILU

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NILUSource {
    companion object {
        @JvmStatic
        suspend fun getForecast(endpoint : String, lat : Double, lon : Double, component : COMPONENTS) : NILUDataClass {
            // build string
            val url = "$endpoint/$lat/$lon/3?method=within&components=component"
            val (req: Request, _: Response, res: String) = Fuel.get(url).awaitStringResponse()
            return Json.decodeFromString(res)
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
        SO2("so2")
    }
}