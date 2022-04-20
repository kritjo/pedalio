package in2000.pedalio.ui.settings

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SettingsKey
import in2000.pedalio.viewmodel.MapViewModel

class SettingsFragment : PreferenceFragmentCompat() {
    private val mapViewModel: MapViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        val gpsPref = findPreference<Preference>(SettingsKey.GPS_TOGGLE.name)
        gpsPref?.setOnPreferenceChangeListener { _, newValue ->
            mapViewModel.shouldGetPermission.postValue(newValue as Boolean)
            true
        }
    }
}