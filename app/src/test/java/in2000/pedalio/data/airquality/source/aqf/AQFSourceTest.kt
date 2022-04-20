package in2000.pedalio.data.airquality.source.aqf

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class AQFSourceTest {
    val ENDPOINT = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/"

    @Test
    fun endpointTest() {
        assertEquals(ENDPOINT, Endpoints.AIRQUALITY_FORECAST)
    }

    @Test
    fun getForecast() {
        val data = runBlocking {
            AQFSource.getForecast(ENDPOINT, 59.92, 10.75)
        }
        assertEquals("59.92000", data.meta.location.latitude)
        assertEquals("10.75000", data.meta.location.longitude)
    }
}