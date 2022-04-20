package in2000.pedalio.data.bikeRoutes.impl

import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.bikeRoutes.BikeRouteRepository
import in2000.pedalio.data.bikeRoutes.source.OsloBikeRoutesSource
import io.data2viz.geojson.FeatureCollection
import io.data2viz.geojson.LineString
import io.data2viz.geojson.toGeoJsonObject
import mil.nga.proj.Projection
import mil.nga.proj.ProjectionFactory
import mil.nga.proj.ProjectionTransform

class OsloBikeRouteRepostiory(val endpoint: String) : BikeRouteRepository() {
    override suspend fun getRoutes(): List<List<LatLng>> {
        val stringRoutes = OsloBikeRoutesSource.getRoutes(endpoint)
            // remove non RFC7946 compliant data
            ?.replace("\"geometry_name\":\"shape\",", "")
            ?.replace(",\"totalFeatures.*".toRegex(), "}") ?: return emptyList()

        // Transform the coordinates to WGS84 using Proj4j
        val epsg25832: Projection? = ProjectionFactory.getProjection(25832)
        val epsg4326: Projection? = ProjectionFactory.getProjection(4326)
        val transformation: ProjectionTransform = epsg25832!!.getTransformation(epsg4326)

        return (stringRoutes.toGeoJsonObject() as FeatureCollection).features.map { feature ->
            (feature.geometry as LineString).coordinates.map {
                val projCord: DoubleArray? = transformation.transform(it[0], it[1])
                LatLng(projCord!![1], projCord[0])
            }
        }
    }
}
