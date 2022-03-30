package in2000.pedalio.data.weather.impl

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class NowcastRepositoryTest {
    val ENDPOINT = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete"

    @Test
    fun getRelativeHumidity() {
        val rh = runBlocking {
            NowcastRepository(ENDPOINT).getRelativeHumidity(59.95, 10.75, 0)
        }
        assertTrue(rh!! in 0.0..100.0)
    }
}