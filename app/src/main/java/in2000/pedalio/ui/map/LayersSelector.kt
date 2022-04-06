package in2000.pedalio.ui.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SharedPreferences

/**
 * A simple [Fragment] subclass.
 * Use the [LayersSelector.newInstance] factory method to
 * create an instance of this fragment.
 */
class LayersSelector : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_layers_selector, container, false)
        val sharedPref = SharedPreferences(requireContext())
        view.findViewById<Switch>(R.id.switch_airquality)
            ?.apply { isChecked = sharedPref.layerAirQuality }
            ?.setOnCheckedChangeListener { _, isChecked -> sharedPref.layerAirQuality = isChecked }
        view.findViewById<Switch>(R.id.switch_weather)
            ?.apply { isChecked = sharedPref.layerWeather }
            ?.setOnCheckedChangeListener { _, isChecked -> sharedPref.layerWeather = isChecked }

        // Inflate the layout for this fragment
        return view
    }
}