package in2000.pedalio.ui.homescreen

import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.search.information.Poi
import com.tomtom.online.sdk.search.location.Address
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult

/**
 * Data class to store a search result.
 * @see SearchResult
 */
data class FavoriteResult(
    val score: Double,
    val address: Address?,
    val position: LatLng,
    val distance: Double,
    val info: String,
    val poi: Poi?,
    /**
     * Icon to show on favorite page.
     */
    val iconSrc: Int = R.drawable.ic_home
)

/**
 * Convert [FavoriteResult] to a [SearchResult].
 */
fun FavoriteResult.toSearchResult() =
    SearchResult(
        score = score,
        address = address,
        position = position,
        distance = distance,
        info = info,
        poi = poi,
    )