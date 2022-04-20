package in2000.pedalio.domain.routing

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.route.information.FullRoute
import in2000.pedalio.data.Endpoints
import in2000.pedalio.data.bikeRoutes.impl.OsloBikeRouteRepostiory
import in2000.pedalio.data.routing.impl.TomtomRoutingRepository
import in2000.pedalio.utils.CoordinateUtil
import java.util.*
import kotlin.math.*

class GetRouteAlternativesUseCase {
    enum class RouteType {
        SHORTEST,
        BIKE
    }

    companion object {
        /**
         * Returns a list of alternative routes, currently shortest and bike route.
         * TODO: Add air quality route
         *
         * @param f the start coordinate
         * @param t the end coordinate
         * @param context
         * @return a list of alternative routes
         */
        suspend fun getRouteAlternatives(
            f: LatLng,
            t: LatLng,
            context: Context
        ): Map<RouteType, FullRoute> {
            val from = LatLng(f.latitude.reduce(4), f.longitude.reduce(4))
            val to = LatLng(t.latitude.reduce(4), t.longitude.reduce(4))

            val bikeRoutesA = OsloBikeRouteRepostiory(Endpoints.OSLO_BIKE_ROUTES).getRoutes()
            val bikeRoutes = mutableListOf<List<LatLng>>()
            bikeRoutesA.forEach { list ->
                bikeRoutes.add(list.map { LatLng(it.latitude.reduce(4), it.longitude.reduce(4)) })
            }

            val routes = TomtomRoutingRepository(context).calculateRoute(from, to)
            val shortest = routes.routes.minByOrNull { it.summary.lengthInMeters }!!


            val leastTimeInBikeRoute = findBikeAlternative(bikeRoutes, context, from, to)
            val leastTimeInBikeRoutePlan = TomtomRoutingRepository(context).calculateRouteFromWaypoints(
                CoordinateUtil.limitPointsOnRouteSimple(leastTimeInBikeRoute, 100)
            ).routes.first()

            return mapOf(Pair(RouteType.BIKE, leastTimeInBikeRoutePlan),
                Pair(RouteType.SHORTEST, shortest))
        }

        /**
         * Finds alternative route that is the most time in bike route.
         * It is a simple algorith that finds the closest point on the route to the start and end
         * point which is on a bike route. It then uses dijkstra to find the shortest path between
         * the points. It also uses tomtom to find the route to the start and end closest points.
         *
         * @param bikeRoutes list of bike routes
         * @param context
         * @param start start point
         * @param end end point
         * @return list of points that is the most time in bike route
         */
        private fun findBikeAlternative(
            bikeRoutes: List<List<LatLng>>,
            context: Context,
            start: LatLng,
            end: LatLng
        ): MutableList<LatLng> {
            val alternativeRoute = mutableListOf<LatLng>()
            val flatMap = bikeRoutes.flatten()

            // Find the closest point in the flatmap to the start
            val closestStart = flatMap.minByOrNull { start.distanceTo(it) }!!
            // Find the closest point in the flatmap to the end
            val closestEnd = flatMap.minByOrNull { end.distanceTo(it) }!!

            // Find a route from start to closestStart
            val startToClosestStart = TomtomRoutingRepository(context).calculateRoute(start, closestStart).routes.first().getCoordinates()
            // Find a route from closestStart to closestEnd
            val closestStartToClosestEnd = dijkstra(
                bikeRoutes,
                LatLng(closestStart.latitude.reduce(4), closestStart.longitude.reduce(4)),
                LatLng(closestEnd.latitude.reduce(4), closestEnd.longitude.reduce(4))
            )
            // Find a route from closestEnd to end
            val closestEndToEnd = TomtomRoutingRepository(context).calculateRoute(closestEnd, end).routes.first().getCoordinates()

            alternativeRoute.addAll(startToClosestStart)
            alternativeRoute.addAll(closestStartToClosestEnd)
            alternativeRoute.addAll(closestEndToEnd)

            return alternativeRoute
        }
    }
}

/**
 * Calculates the distance between two points on the earth
 *
 * @param it the second point
 * @return the distance in meters
 */
private fun LatLng.distanceTo(it: LatLng): Double {
    val earthRadius = 6371000.0

    val dLat = Math.toRadians(it.latitude - this.latitude)
    val dLon = Math.toRadians(it.longitude - this.longitude)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            sin(dLon / 2) * sin(dLon / 2) *
            cos(Math.toRadians(this.latitude)) *
            cos(Math.toRadians(it.latitude))

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}

/**
 * Finds the shortest route between two points using Dijkstra's algorithm.
 *
 * @param bikeRoutes A list of routes that can be used to travel between the two points.
 * @param start The starting point.
 * @param end The ending point.
 * @return A list of coordinates that represent the shortest route between the two points.
 */
private fun dijkstra(
    bikeRoutes: List<List<LatLng>>,
    start: LatLng,
    end: LatLng
): List<LatLng> {
    // Calculate the graph
    val vertices = bikeRoutes.flatten().toSet()
    val edges = mutableSetOf<Pair<LatLng, LatLng>>()
    bikeRoutes.forEach {
        edges.addAll(it.zipWithNext())
    }

    // Add edges between different routes where the distance is less than 100 meters
    // I know, this is slow, but it works
    vertices.forEach { from ->
        vertices.forEach { to ->
            if (from.distanceTo(to) < 100) {
                edges.add(Pair(from, to))
            }
        }
    }

    val reversedEdges = mutableSetOf<Pair<LatLng, LatLng>>()
    edges.forEach { reversedEdges.add(Pair(it.second, it.first)) }
    edges.addAll(reversedEdges)

    val q = PriorityQueue { a: Pair<Int, LatLng>, b: Pair<Int, LatLng> -> a.first.compareTo(b.first) }
    val distances = mutableMapOf<LatLng, Int>()
    vertices.forEach { distances[it] = Int.MAX_VALUE }
    val parents = mutableMapOf<LatLng, LatLng?>()
    q.add(Pair(0, start))
    distances[start] = 0
    parents[start] = null
    while (q.isNotEmpty()) {
        val (distance, vertex) = q.remove()
        for (edge in edges) {
            if (edge.first == vertex) {
                val newDistance = distance + 1
                if (newDistance < distances[edge.second]!!) {
                    distances[edge.second] = newDistance
                    parents[edge.second] = vertex
                    q.add(Pair(newDistance, edge.second))
                }
            }
        }
    }

    // Find the shortest path
    val path = mutableListOf<LatLng>()
    var current: LatLng? = end
    while (current != null) {
        path.add(current)
        current = parents[current]
    }
    return path.reversed()
}

/**
 * This function reduces the number of decimal places in a double
 *
 * @param decp the number of decimal places to reduce to
 * @return the reduced double
 */
private fun Double.reduce(decp: Int): Double {
    var value = this
    value *= 10.0.pow(decp)
    value = floor(value)
    value /= 10.0.pow(decp)
    return value
}