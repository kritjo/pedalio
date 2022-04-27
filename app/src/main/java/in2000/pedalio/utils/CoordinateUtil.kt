package in2000.pedalio.utils

import com.tomtom.online.sdk.common.location.LatLng
import kotlin.math.*

class CoordinateUtil {
    companion object {
        /**
         * Limits points on a route (List of LatLng) to a certain amount
         * @param points List of LatLng
         * @param maxPoints Amount of points to limit to
         * @return New list of LatLng
         */
        fun limitPointsOnRouteSimple(points: List<LatLng>, maxPoints: Int): List<LatLng> {
            // naive implementation, in future we can use a more sophisticated algorithm which takes into account the distance between points
            if (points.size <= maxPoints) {
                return points
            }
            val result = mutableListOf<LatLng>()
            val step: Int = ceil(points.size.toDouble() / maxPoints.toDouble()).toInt()
            for (i in points.indices step step) {
                result.add(points[i])
            }
            return result
        }

        /**
         * Finds the distance between two polar coordinates
         * @param a Pair of polar coordinates
         * @param b Pair of polar coordinates
         * @return Distance between a and b
         * Math doc: https://www.geeksforgeeks.org/program-distance-two-points-earth/
         */
        fun calcDistanceBetweenTwoCoordinates(a: LatLng, b: LatLng): Double {
            val lon1 = Math.toRadians(a.longitude)
            val lon2 = Math.toRadians(b.longitude)
            val lat1 = Math.toRadians(a.latitude)
            val lat2 = Math.toRadians(b.latitude)
            val dlon = lon2 - lon1
            val dlat = lat2 - lat1
            val aa = sin(dlat / 2).pow(2.0) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2.0)
            val c = 2 * asin(sqrt(aa))
            return 6378.8 * c
        }
    }
}