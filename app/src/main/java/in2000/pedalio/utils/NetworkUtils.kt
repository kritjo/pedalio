package in2000.pedalio.utils

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitStringResponse

class NetworkUtils {
    companion object {
        @JvmStatic
        suspend fun isNetworkAvailable(): Boolean {
            return try {
                val (_: Request, res: Response, _: String) = Fuel.get("http://google.com").awaitStringResponse()
                res.statusCode in 200..399
            } catch (e: FuelError) {
                false
            }
        }
    }
}