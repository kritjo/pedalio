package in2000.pedalio.data.airquality.impl

import android.util.Log
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class AQFRepositoryTest {
    val ENDPOINT = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/"

    @Test
    fun getAQI() {
        val AQI = runBlocking {
            AQFRepository(ENDPOINT).getAQI(59.92, 10.75, 0)
        }
        assertTrue(AQI in 1.0..5.0)
    }

}