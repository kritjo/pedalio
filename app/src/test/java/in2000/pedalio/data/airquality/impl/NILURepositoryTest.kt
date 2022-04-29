package in2000.pedalio.data.airquality.impl

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class NILURepositoryTest {
    private val endpoint = Endpoints.NILU_FORECAST

    @Test
    fun getAQI() {
        val aqi = runBlocking {
            NILURepository(endpoint, 3).getAQI(59.92, 10.75, 0)
        }
        assertTrue(aqi in 1.0..5.0)
    }
}