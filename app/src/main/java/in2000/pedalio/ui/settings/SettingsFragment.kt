package in2000.pedalio.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SettingsKey
import in2000.pedalio.data.settings.impl.SharedPreferences
import in2000.pedalio.viewmodel.MapViewModel

class SettingsFragment : PreferenceFragmentCompat() {
    private val mapViewModel: MapViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        val gpsPref = findPreference<Preference>(SettingsKey.GPS_TOGGLE.name)
        val sharedPreferences = SharedPreferences(requireContext())
        val theme = findPreference<Preference>(SettingsKey.THEME.name)
        val followSystem = findPreference<Preference>(SettingsKey.FOLLOW_SYS.name)

        gpsPref?.setOnPreferenceChangeListener { _, newValue ->
            mapViewModel.shouldGetPermission.postValue(newValue as Boolean)
            true
        }

        theme?.setOnPreferenceChangeListener { _, newValue ->
            sharedPreferences.theme = newValue as Boolean
            // Check if we should follow system settings, if so do not care about setting
            if (sharedPreferences.followSystem) {
                return@setOnPreferenceChangeListener true
            }
            if (newValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

        followSystem?.setOnPreferenceChangeListener { _, newValue ->
            sharedPreferences.followSystem = newValue as Boolean
            when {
                newValue -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    theme?.isEnabled = false
                }
                sharedPreferences.theme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    theme?.isEnabled = true
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    theme?.isEnabled = true
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if theme switch should be enabled. Should only be enabled when "Follow sys settings" is off
        val sharedPreferences = SharedPreferences(requireContext())
        val theme = findPreference<Preference>(SettingsKey.THEME.name)
        theme?.isEnabled = !sharedPreferences.followSystem
    }
}