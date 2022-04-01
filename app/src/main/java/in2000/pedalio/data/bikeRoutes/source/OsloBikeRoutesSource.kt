package in2000.pedalio.data.bikeRoutes.source

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponse

class OsloBikeRoutesSource {
    companion object {
        @JvmStatic
        suspend fun getRoutes(
            endpoint: String
        ): String {
            // Third is the result
            return Fuel.get(endpoint).awaitStringResponse().third
        }
    }
}