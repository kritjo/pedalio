package in2000.pedalio.data.settings.impl

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.data.settings.SettingsRepository
import in2000.pedalio.ui.homescreen.FavoriteResult

/**
 * Repository for settings. Uses shared preferences to store settings.
 * @see [SettingsKey]
 */
class SharedPreferences(context: Context) : SettingsRepository() {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    override var distanceUnit: String?
        get() = sharedPreferences.getString(SettingsKey.DISTANCE_UNIT.name, "")
        set(value) = sharedPreferences.edit().putString(SettingsKey.DISTANCE_UNIT.name, value)
            .apply()

    override var theme: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.THEME.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.THEME.name, value).apply()

    override var colorBlindMode: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.COLOR_BLIND_MODE.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.COLOR_BLIND_MODE.name, value)
            .apply()

    override var gpsToggle: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.GPS_TOGGLE.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.GPS_TOGGLE.name, value).apply()

    override var askedForGps: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.ASKED_FOR_GPS.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.ASKED_FOR_GPS.name, value)
            .apply()

    override var shownWelcomeScreen: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.SHOWN_WELCOME_SCREEN.name, false)
        set(value) = sharedPreferences.edit()
            .putBoolean(SettingsKey.SHOWN_WELCOME_SCREEN.name, value).apply()

    override var layerAirQuality: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.LAYER_AIR_QUALITY.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.LAYER_AIR_QUALITY.name, value)
            .apply()

    override var layerWeather: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.LAYER_WEATHER.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.LAYER_WEATHER.name, value)
            .apply()

    override var layerBikeRoutes: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.LAYER_BIKE_ROUTES.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.LAYER_BIKE_ROUTES.name, value)
            .apply()

    /**
     * Should use [appendRecentSearch] instead of using this directly.
     */
    override var recentSearches: List<SearchResult>
        get(): List<SearchResult> {
            val saved = sharedPreferences.getString(SettingsKey.RECENT_SEARCHES.name, "") ?: ""
            if (saved == "") return emptyList()

            val lClass = TypeToken.getParameterized(List::class.java, SearchResult::class.java).type
            return Gson().fromJson(saved, lClass) ?: emptyList()
        }
        set(value) = sharedPreferences.edit().putString(
            SettingsKey.RECENT_SEARCHES.name, Gson().toJson(value)
        ).apply()

    fun appendRecentSearch(searchResult: SearchResult) {
        // return if duplicate
        if (recentSearches.contains(searchResult)) return
        recentSearches = recentSearches.toMutableList().apply {
            if (size > 9) removeAt(9) // remove last if list is full
            add(0, searchResult)
        }
    }

    /**
     * Should use [appendFavoriteSearch] and [removeFavorite] instead of using directly.
     */
    override var favoriteSearches: List<FavoriteResult>
        get(): List<FavoriteResult> {
            val saved = sharedPreferences.getString(SettingsKey.FAVORITE_SEARCHES.name, "") ?: ""
            if (saved == "") return emptyList()

            val lClass =
                TypeToken.getParameterized(List::class.java, FavoriteResult::class.java).type
            return Gson().fromJson(saved, lClass) ?: emptyList()
        }
        set(value) = sharedPreferences.edit().putString(
            SettingsKey.FAVORITE_SEARCHES.name, Gson().toJson(value)
        ).apply()

    fun appendFavoriteSearch(favoriteResult: FavoriteResult) {
        // return if duplicate
        if (favoriteSearches.contains(favoriteResult)) return
        favoriteSearches = favoriteSearches.toMutableList().apply {
            if (size > 9) removeAt(9)
            add(0, favoriteResult)
        }
    }

    fun removeFavorite(favoriteResult: FavoriteResult) {
        favoriteSearches = favoriteSearches.toMutableList().apply { remove(favoriteResult) }
    }
}

enum class SettingsKey {
    /**
     * The key for the setting that stores the current theme.
     */
    THEME,

    /**
     * The key for the setting that stores the selected distance unit.
     */
    DISTANCE_UNIT,

    /**
     * The key for the setting that stores the color blind mode.
     */
    COLOR_BLIND_MODE,

    /**
     * The key for the setting that stores the GPS toggle.
     */
    GPS_TOGGLE,

    /**
     * The key for the setting that stores the asked for GPS.
     */
    ASKED_FOR_GPS,

    /**
     * The key for the setting that stores the shown welcome screen setting.
     */
    SHOWN_WELCOME_SCREEN,

    /**
     * The keys for layer settings
     */
    LAYER_AIR_QUALITY,

    /**
     * The key for the setting that stores whether the layer for the weather is enabled.
     */
    LAYER_WEATHER,

    /**
     * The key for the setting that stores the bike routes layer toggle.
     */
    LAYER_BIKE_ROUTES,

    /**
     * The key for the setting that stores the recent searches.
     */
    RECENT_SEARCHES,

    /**
     * The key for the setting that stores the favorite searches.
     */
    FAVORITE_SEARCHES
}