package in2000.pedalio.data.settings.impl

import android.content.Context
import androidx.preference.PreferenceManager
import in2000.pedalio.data.settings.SettingsRepository

/**
 * Repository for settings. Uses shared preferences to store settings.
 */
class SharedPreferences(context: Context): SettingsRepository() {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    override var distanceUnit: String?
        get() = sharedPreferences.getString(SettingsKey.DISTANCE_UNIT.name, "")
        set(value) = sharedPreferences.edit().putString(SettingsKey.DISTANCE_UNIT.name, value).apply()

    override var theme: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.THEME.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.THEME.name, value).apply()

    override var colorBlindMode: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.COLOR_BLIND_MODE.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.COLOR_BLIND_MODE.name, value).apply()

    override var gpsToggle: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.GPS_TOGGLE.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.GPS_TOGGLE.name, value).apply()

    override var askedForGps: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.ASKED_FOR_GPS.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.ASKED_FOR_GPS.name, value).apply()

    override var layerAirQuality: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.LAYER_AIR_QUALITY.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.LAYER_AIR_QUALITY.name, value).apply()

    override var layerWeather: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.LAYER_WEATHER.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.LAYER_WEATHER.name, value).apply()
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
     * The keys for layer settings
     */
    LAYER_AIR_QUALITY,
    LAYER_WEATHER
}