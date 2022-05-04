package in2000.pedalio.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import in2000.pedalio.R
import in2000.pedalio.data.settings.impl.SharedPreferences
import in2000.pedalio.viewmodel.MapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Overlay that allows the user to select which layers are displayed on the map.
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
        view.findViewById<Switch>(R.id.switch_bikeRoutes)
            ?.apply { isChecked = sharedPref.layerBikeRoutes }
            ?.setOnCheckedChangeListener { _, isChecked -> sharedPref.layerBikeRoutes = isChecked }
        view.findViewById<Spinner>(R.id.aq_spinner).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // get selected item string
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    sharedPref.layerAQComponent = selectedItem
                    val mapViewModel: MapViewModel by activityViewModels()
                    mapViewModel.parseAndUpdateComponentFromPreferences()
                    lifecycleScope.launch(Dispatchers.IO) {
                        mapViewModel.updateAirQuality(mapViewModel.aqComponent)
                        mapViewModel.createAQPolygons(mapViewModel.getAirQuality())
                    }
                }
            }

        view.findViewById<Spinner>(R.id.aq_spinner).setSelection( // get from SharedPreferences
            sharedPref.layerAQComponent.let {
                when (it) {
                    "NO" -> 0
                    "PM2.5" -> 1
                    "PM10" -> 2
                    "AQI" -> 3
                    else -> 0
                }
            }
        )
        return view
    }
}