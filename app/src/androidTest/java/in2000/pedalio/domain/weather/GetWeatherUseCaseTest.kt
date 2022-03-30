package in2000.pedalio.domain.weather
import org.junit.Test
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.Endpoints
import in2000.pedalio.data.weather.impl.LocationforecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert

class GetWeatherUseCaseTest {
    @Test
    fun testGetWeatherUseCase() {
        val latLng = LatLng(59.92, 10.75)
        val locationforecastRepository = LocationforecastRepository(Endpoints.LOCATIONFORECAST_COMPLETE)
        val nowcastRepository = NowcastRepository(Endpoints.NOWCAST_COMPLETE)
        val getWeatherUseCase = GetWeatherUseCase(nowcastRepository, locationforecastRepository)
        runBlocking {
            var dp = getWeatherUseCase.getWeather(latLng)
            Assert.assertTrue(dp.temperature ?: 0.0 in -20.0..40.0)
            Assert.assertTrue(dp.humidity ?: 0.0 in 0.0..100.0)
            Assert.assertTrue(dp.percipitation ?: 0.0 in 0.0..1000.0)

            dp = getWeatherUseCase.getWeather(latLng, timeDelta = 30)
            Assert.assertTrue(dp.temperature?: 0.0 in -20.0..40.0)
            Assert.assertTrue(dp.humidity?: 0.0 in 0.0..100.0)
            Assert.assertTrue(dp.percipitation?: 0.0 in 0.0..100.0)

            dp = getWeatherUseCase.getWeather(latLng, timeDelta = 120)
            Assert.assertTrue(dp.temperature?: 0.0 in -20.0..40.0)
            Assert.assertTrue(dp.humidity?: 0.0 in 0.0..100.0)
            Assert.assertTrue(dp.percipitation?: 0.0 in 0.0..100.0)
        }
    }
}
