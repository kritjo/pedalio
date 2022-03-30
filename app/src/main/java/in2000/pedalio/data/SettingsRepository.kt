package in2000.pedalio.data

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import java.util.prefs.PreferenceChangeEvent

/**
 * Repository for settings. Uses shared preferences to store settings.
 */
class SettingsRepository(context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var distanceUnit: String?
        get() = sharedPreferences.getString(SettingsKey.DISTANCE_UNIT.name, "")
        set(value) = sharedPreferences.edit().putString(SettingsKey.DISTANCE_UNIT.name, value).apply()

    var theme: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.THEME.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.THEME.name, value).apply()

    var colorBlindMode: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.COLOR_BLIND_MODE.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.COLOR_BLIND_MODE.name, value).apply()

    var gpsToggle: Boolean
        get() = sharedPreferences.getBoolean(SettingsKey.GPS_TOGGLE.name, false)
        set(value) = sharedPreferences.edit().putBoolean(SettingsKey.GPS_TOGGLE.name, value).apply()

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
}