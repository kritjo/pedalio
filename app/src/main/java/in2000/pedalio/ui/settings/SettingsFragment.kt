package in2000.pedalio.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import in2000.pedalio.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}