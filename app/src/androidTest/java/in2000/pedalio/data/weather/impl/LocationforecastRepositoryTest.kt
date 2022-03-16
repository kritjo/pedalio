package in2000.pedalio.data.weather.impl

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class LocationforecastRepositoryTest {
    val ENDPOINT = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/complete"

    @Test
    fun getTemp() {
        val temp = runBlocking {
            LocationforecastRepository(ENDPOINT).getTemp(59.92, 10.75, 0)
        }
        assertTrue(temp!! in -50.0..50.0)
    }
}