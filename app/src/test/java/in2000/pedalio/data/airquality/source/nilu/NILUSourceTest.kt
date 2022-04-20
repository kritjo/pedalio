package in2000.pedalio.data.airquality.source.nilu

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class NILUSourceTest {
    val ENDPOINT = Endpoints.NILU_FORECAST

    @Test
    fun endpointTest() {
        assertEquals(ENDPOINT, Endpoints.NILU_FORECAST)
    }

    @Test
    fun getNow() {
        val data = runBlocking {
            NILUSource.getNow(ENDPOINT, 59.92, 10.75, 3, NILUSource.COMPONENTS.ALL)
        }
        if (data == null) {
            fail("Data is null")
        } else {
            assertEquals("Oslo", data[0].municipality)
        }
    }
}