package in2000.pedalio.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import in2000.pedalio.R

class SettingsFragment : PreferenceFragmentCompat() {

    // TODO: Close app when gps toggle is turned on and off or fix gps live updates
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}