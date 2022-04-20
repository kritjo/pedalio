package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.Endpoints
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class LocationForecastRepositoryTest {
    val ENDPOINT = Endpoints.LOCATIONFORECAST_COMPLETE

    @Test
    fun getTemp() {
        val temp = runBlocking {
            LocationForecastRepository(ENDPOINT).getTemp(59.92, 10.75, 0)
        }
        assertTrue(temp!! in -50.0..50.0)
    }
}