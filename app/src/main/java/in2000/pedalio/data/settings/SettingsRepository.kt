package in2000.pedalio.data.settings

import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.ui.homescreen.FavoriteResult

abstract class SettingsRepository {
    abstract var layerBikeRoutes: Boolean
    abstract var distanceUnit: String?
    abstract var theme: Boolean
    abstract var colorBlindMode: Boolean
    abstract var gpsToggle: Boolean
    abstract var askedForGps: Boolean
    abstract var shownWelcomeScreen: Boolean
    abstract var layerAirQuality: Boolean
    abstract var layerWeather: Boolean
    abstract var recentSearches: List<SearchResult>
    abstract var favoriteSearches: List<FavoriteResult>
}