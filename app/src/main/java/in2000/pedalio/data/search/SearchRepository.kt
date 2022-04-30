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
    /**
     * @param fuzzySearchSpecification specification of the search to be done.
     * @return A list of search results or null if no matches.
     */
    abstract fun doSearch(fuzzySearchSpecification: FuzzySearchSpecification): List<SearchResult>?
}

/**
 * Data class of a search result.
 */
data class SearchResult(
    /**
     * The certainty that this is the correct result.
     */
    val score: Double,
    val address: Address?,
    val position: LatLng,
    val distance: Double,
    val info: String,
    val poi: Poi?
)