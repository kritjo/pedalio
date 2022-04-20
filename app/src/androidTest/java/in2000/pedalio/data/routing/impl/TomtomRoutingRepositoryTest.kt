package in2000.pedalio.data.routing.impl

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.RoutePlan
import org.junit.Assert
import org.junit.Test

class TomtomRoutingRepositoryTest {
    @Test
    fun calculateRoute() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val routing  = TomtomRoutingRepository(appContext)
        val origin = LatLng(59.95901810547076, 10.737168932389016)
        val destination = LatLng(59.95275321623974, 10.748902973073466)
        val plan : RoutePlan? = routing.calculateRoute(origin, destination)
        plan?.routes?.forEach {
            Assert.assertTrue(it.summary.lengthInMeters in 100..2000)
        } ?: Assert.fail("No route found")
    }

    @Test
    fun calculateRouteFromWaypoints(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val routing  = TomtomRoutingRepository(appContext)
        val origin =        LatLng(59.95901810547076, 10.737168932389016)
        val middle =        LatLng(59.95896083733742, 10.742256703144921)
        val destination =   LatLng(59.95275321623974, 10.748902973073466)
        val waypoints = listOf(origin, middle, destination)
        val plan : RoutePlan? = routing.calculateRouteFromWaypoints(waypoints)
        plan?.routes?.forEach {
            Assert.assertTrue(it.summary.lengthInMeters in 100..2000)
        } ?: Assert.fail("No route found")
    }
}