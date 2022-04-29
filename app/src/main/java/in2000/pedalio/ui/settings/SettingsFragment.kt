package in2000.pedalio.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SettingsKey
import in2000.pedalio.viewmodel.MapViewModel

class SettingsFragment : PreferenceFragmentCompat() {
    private val mapViewModel : MapViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        val gpsPref = findPreference<Preference>(SettingsKey.GPS_TOGGLE.name)

        val sharedPreferences = SharedPreferences(requireContext())
        val theme = findPreference<Preference>(SettingsKey.THEME.name)

        gpsPref?.setOnPreferenceChangeListener { _, newValue ->
            mapViewModel.shouldGetPermission.postValue(newValue as Boolean)
            true
        }

        theme?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.theme = true
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.theme = false
            }

            true
        }
    }
}