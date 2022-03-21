package in2000.pedalio.utils

import com.tomtom.online.sdk.common.location.LatLng
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CoordinateUtil {
    companion object{
        fun limitPointsOnRoute(points: List<LatLng>, maxPoints : Int): List<LatLng> {
            // naive implementation, in future we can use a more sophisticated algorithm which takes into account the distance between points
            if (points.size <= maxPoints) {
                return points
            }
            val result = mutableListOf<LatLng>()
            val step : Int = points.size / maxPoints
            for (i in points.indices step step) {
                result.add(points[i])
            }
            return result
        }

        // https://www.geeksforgeeks.org/program-distance-two-points-earth/
        fun calcDistanceBetweenTwoCoordinates(a : LatLng, b : LatLng) : Double {
            val lon1 = Math.toRadians(a.longitude)
            val lon2 = Math.toRadians(b.longitude)
            val lat1 = Math.toRadians(a.latitude)
            val lat2 = Math.toRadians(b.latitude)
            val dlon = lon2 - lon1
            val dlat = lat2 - lat1
            val aa = Math.pow(sin(dlat / 2), 2.0) + cos(lat1) * cos(lat2) * Math.pow(sin(dlon / 2), 2.0)
            val c = 2 * asin(sqrt(aa))
            return 6371 * c
        }
    }
}