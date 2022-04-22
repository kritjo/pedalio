package in2000.pedalio.data.search

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification
import com.tomtom.online.sdk.search.information.Poi
import com.tomtom.online.sdk.search.location.Address

/**
 * Search repository.
 * get all the search results from the tomtom api
 */

abstract class SearchRepository {
    abstract fun doSearch(fuzzySearchSpecification: FuzzySearchSpecification): List<SearchResult>?
}

data class SearchResult(
    val score: Double,
    val address: Address?,
    val position: LatLng,
    val distance: Double,
    val info: String,
    val poi: Poi?
)