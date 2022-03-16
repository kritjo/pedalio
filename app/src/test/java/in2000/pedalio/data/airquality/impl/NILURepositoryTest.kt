package in2000.pedalio.data.airquality.impl

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class NILURepositoryTest {
    val ENDPOINT = "https://api.nilu.no/aq/utd"

    @Test
    fun getAQI() {
        val AQI = runBlocking {
            NILURepository(ENDPOINT, 3).getAQI(59.92, 10.75, 0)
        }
        assertTrue(AQI in 1.0..5.0)
    }
}