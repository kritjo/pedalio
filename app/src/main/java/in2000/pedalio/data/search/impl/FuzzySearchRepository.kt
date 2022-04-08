package in2000.pedalio.data.search.impl

import android.content.Context
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchApi
import com.tomtom.online.sdk.search.fuzzy.*
import in2000.pedalio.data.search.SearchRepository
import in2000.pedalio.data.search.SearchResult

/**
 * Fuzzy search repository.
 * @param context Context
 * @param maxFuzzyLevel The maximum fuzzy level. See https://developer.tomtom.com/maps-android-sdk/documentation/search/search-examples/fuzziness-parameter
 */
class FuzzySearchRepository (context : Context) : SearchRepository() {
    private var searchApi : SearchApi
    init {
        searchApi = OnlineSearchApi.create(context, "beN1MD9T81Hr774H5o2lQGGDywkiqcJ8")
    }

    /**
     * Perform fuzzy search based on the search specification.
     *
     * @param fuzzySearchSpecification
     */
    override fun doSearch(fuzzySearchSpecification : FuzzySearchSpecification) : List<SearchResult> {
        val results = searchApi.search(fuzzySearchSpecification).value()
        val searchResults = mutableListOf<SearchResult>()
        for (result in results.fuzzyDetailsList) {
            val searchResult = SearchResult(result.score, result.address, result.position, result.distance, result.info, result.poi)
            searchResults.add(searchResult)
        }
        return searchResults
    }
}