package in2000.pedalio.utils
import com.tomtom.online.sdk.common.location.LatLng
import kotlin.math.pow


class MathUtil {
    companion object {
      private const val POWER = 2
      // Docs : https://gisgeography.com/inverse-distance-weighting-idw-interpolation/#:~:text=Interpolated%20points%20are%20estimated%20based,has%20from%20the%20known%20values.
        fun inverseDistanceWeighting(here : LatLng, stations : List<Pair<LatLng, Double>>): Double {
          var sum = 0.0
          var sumDx = 0.0
          for (i in stations.indices) {
            val dist = CoordinateUtil.calcDistanceBetweenTwoCoordinates(here, stations[i].first)
            val value = stations[i].second
            sum += value / dist.pow(POWER)
            sumDx += 1.0 / dist.pow(POWER)
          }

          return sum / sumDx
        }
    }
}