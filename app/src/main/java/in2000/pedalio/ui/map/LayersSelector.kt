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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LayersSelector.newInstance] factory method to
 * create an instance of this fragment.
 */
class LayersSelector : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LayersSelector.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LayersSelector().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}