package in2000.pedalio.data.search.impl

import android.content.Context
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchApi
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification
import in2000.pedalio.data.search.SearchRepository
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.utils.NetworkUtils
import kotlinx.coroutines.runBlocking

/**
 * Fuzzy search repository.
 */
class FuzzySearchRepository(context: Context) : SearchRepository() {
    private var searchApi: SearchApi =
        OnlineSearchApi.create(context, "beN1MD9T81Hr774H5o2lQGGDywkiqcJ8")

    /**
     * Perform fuzzy search based on the search specification.
     *
     * @param fuzzySearchSpecification
     */
    override fun doSearch(fuzzySearchSpecification: FuzzySearchSpecification): List<SearchResult>? {
        val networkAvailable = runBlocking { return@runBlocking NetworkUtils.isNetworkAvailable() }
        if (!networkAvailable) return null

        if (fuzzySearchSpecification.term.isEmpty()) return emptyList()
        val results = searchApi.search(fuzzySearchSpecification).value()
        val searchResults = mutableListOf<SearchResult>()
        for (result in results.fuzzyDetailsList) {
            searchResults.add(
                SearchResult(
                    result.score,
                    result.address,
                    result.position,
                    result.distance,
                    result.info,
                    result.poi
                )
            )
        }
        return searchResults
    }
}