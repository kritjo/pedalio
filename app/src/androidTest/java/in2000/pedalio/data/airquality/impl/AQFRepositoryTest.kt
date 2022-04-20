package in2000.pedalio.data.airquality.impl

import android.util.Log
import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class AQFRepositoryTest {
    val ENDPOINT = Endpoints.AIRQUALITY_FORECAST

    @Test
    fun getAQI() {
        val AQI = runBlocking {
            AQFRepository(ENDPOINT).getAQI(59.92, 10.75, 0)
        }
        assertTrue(AQI in 1.0..5.0)
    }

}