package in2000.pedalio.data.airquality.source.NILU

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class NILUSourceTest {
    val ENDPOINT = "https://api.nilu.no/aq/utd"

    @Test
    fun endpointTest() {
        assertEquals(ENDPOINT, Endpoints.NILU_FORECAST)
    }

    @Test
    fun getNow() {
        val data = runBlocking {
            NILUSource.getNow(ENDPOINT, 59.92, 10.75, 3, NILUSource.COMPONENTS.ALL)
        }
        assertEquals("Oslo", data[0].municipality)
    }
}