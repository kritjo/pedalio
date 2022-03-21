package in2000.pedalio.utils

import com.tomtom.online.sdk.common.location.LatLng

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
    }
}