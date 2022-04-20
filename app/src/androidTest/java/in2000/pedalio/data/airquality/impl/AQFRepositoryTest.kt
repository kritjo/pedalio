package in2000.pedalio.data.airquality.impl

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class AQFRepositoryTest {
    private val endpoint = Endpoints.AIRQUALITY_FORECAST

    @Test
    fun getAQI() {
        val aqi = runBlocking {
            AQFRepository(endpoint).getAQI(59.92, 10.75, 0)
        }
        assertTrue(aqi in 1.0..5.0)
    }

}