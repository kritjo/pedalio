package in2000.pedalio.data.airquality.source.aqf

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class AQFSourceTest {
    private val endpoint = Endpoints.AIRQUALITY_FORECAST

    @Test
    fun endpointTest() {
        assertEquals(endpoint, Endpoints.AIRQUALITY_FORECAST)
    }

    @Test
    fun getForecast() {
        val data = runBlocking {
            AQFSource.getForecast(endpoint, 59.92, 10.75)
        }
        assertEquals("59.92000", data?.meta?.location?.latitude)
        assertEquals("10.75000", data?.meta?.location?.longitude)
    }
}