package in2000.pedalio.data.settings

import in2000.pedalio.data.airquality.source.nilu.NILUSource
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.ui.homescreen.FavoriteResult

abstract class SettingsRepository {
    /**
     * Follow system theme (dark or light mode). Light is false, dark is true.
     */
    abstract var followSystem: Boolean

    /**
     * Is the bike route layer turned on?
     */
    abstract var layerBikeRoutes: Boolean

    /**
     * Theme (dark or light mode). Light is false, dark is true.
     */
    abstract var theme: Boolean

    /**
     * Color blind mode on/off.
     */
    abstract var colorBlindMode: Boolean

    /**
     * Should use user-location?
     */
    abstract var gpsToggle: Boolean

    /**
     * Have we asked for GPS permission?
     */
    abstract var askedForGps: Boolean

    /**
     * Have we shown first boot screen?
     */
    abstract var shownWelcomeScreen: Boolean

    /**
     * Is the Air Quality layer turned on?
     */
    abstract var layerAirQuality: Boolean

    /**
     * Is the weather layer turned on?
     */
    abstract var layerWeather: Boolean

    /**
     * List of users recent searches
     */
    abstract var recentSearches: List<SearchResult>

    /**
     * List of users favorite places.
     */
    abstract var favoriteSearches: List<FavoriteResult>

    /**
     * Which AQ layer to show?
     */
    abstract var layerAQComponent: String?
}