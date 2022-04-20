package in2000.pedalio.domain.routing

import androidx.test.platform.app.InstrumentationRegistry
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan
import in2000.pedalio.data.Endpoints
import in2000.pedalio.data.routing.impl.TomtomRoutingRepository
import in2000.pedalio.data.weather.impl.LocationforecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository
import in2000.pedalio.domain.weather.GetWeatherUseCase
import in2000.pedalio.utils.CoordinateUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetWeatherOnRouteUseCaseTest {
    @Test
    fun getBatchWeather(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val routing  = TomtomRoutingRepository(appContext)
        val origin =        LatLng(59.95901810547076, 10.737168932389016)
        val middle =        LatLng(59.95896083733742, 10.742256703144921)
        val destination =   LatLng(59.95275321623974, 10.748902973073466)
        val waypoints = listOf(origin, middle, destination)
        val plan : RoutePlan? = routing.calculateRouteFromWaypoints(waypoints)
        if (plan == null) {
            Assert.fail("No route found")
            return
        }
        val route = plan.routes[0]
        val routePoints = CoordinateUtil.limitPointsOnRouteSimple(route.getCoordinates(), 10)
        Assert.assertTrue(routePoints.size in 7..12)
        val nowcast = NowcastRepository(Endpoints.NOWCAST_COMPLETE)
        val locationforecast = LocationforecastRepository(Endpoints.LOCATIONFORECAST_COMPLETE)
        val weatherUseCase = GetWeatherUseCase(nowcast, locationforecast)
        val weatherOnRouteUseCase = GetWeatherOnRouteUseCase(weatherUseCase, context = appContext)
        runBlocking {
            val weatherOnRoute = weatherOnRouteUseCase.getBatchWeather(routePoints)
            if (weatherOnRoute != null) {
                Assert.assertTrue(weatherOnRoute.size in 7..12)
                weatherOnRoute.forEach() {
                    Assert.assertTrue(it.temperature!! in -20f..40f)
                }
            }
        }
    }
}