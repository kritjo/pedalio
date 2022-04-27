package in2000.pedalio.data.search.source

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.common.location.LatLngBias
import com.tomtom.online.sdk.search.fuzzy.FuzzyLocationDescriptor
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchEngineDescriptor
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification

class FuzzySearchSource(private val maxFuzzyLevel: Int = 2) {
    /**
     * Get FuzzySearchEngineDescriptor for a given location.
     *
     * @param position Position used to bias the results.
     * @param radius The radius in meters. Default value is 0.
     */
    private fun getLocationDescriptor(position: LatLng, radius: Double): FuzzyLocationDescriptor {
        val location = LatLngBias(position, radius)
        return FuzzyLocationDescriptor.Builder()
            .positionBias(location)
            .build()
    }

    /**
     * Create a FuzzySearchSpecification for a given search query and location.
     * @param searchQuery The search query.
     * @param position Position used to bias the results.
     * @param radius The radius in meters. Default value is 30000.0
     */
    fun createSpecification(
        searchQuery: String,
        position: LatLng,
        radius: Double = 35000.0
    ): FuzzySearchSpecification {
        val searchEngineDescriptor = FuzzySearchEngineDescriptor.Builder()
            .minFuzzyLevel(1)
            .maxFuzzyLevel(maxFuzzyLevel)
            .language("no-NO")
            .build()

        val fuzzyLocationDescriptor = getLocationDescriptor(position, radius)

        return FuzzySearchSpecification.Builder(searchQuery)
            .searchEngineDescriptor(searchEngineDescriptor)
            .locationDescriptor(fuzzyLocationDescriptor)
            .build()
    }

}