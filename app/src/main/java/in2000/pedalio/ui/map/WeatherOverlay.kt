package in2000.pedalio.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import in2000.pedalio.R
import in2000.pedalio.viewmodel.MapViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherOverlay.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherOverlay : Fragment() {
    private val mapViewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather_overlay, container, false)

        mapViewModel.weather.observe(viewLifecycleOwner) {
            // TODO: Choose icons
            view.findViewById<TextView>(R.id.weather_degrees).text = resources.getString(R.string.degrees,
                it.temperature?.toInt() ?: 0, (it.temperature?.minus(it.temperature.toInt())
                    ?.times(10))?.toInt() ?: 0
            )
            view.findViewById<TextView>(R.id.weather_wind).text = resources.getString(R.string.m_s,
                it.windSpeed?.toInt() ?: 0, (it.windSpeed?.minus(it.windSpeed.toInt())
                ?.times(10))?.toInt() ?: 0
            )
            view.findViewById<TextView>(R.id.percipation).text = resources.getString(R.string.mm_h,
                it.percipitation?.toInt() ?: 0, (it.windSpeed?.minus(it.windSpeed.toInt())
                ?.times(10))?.toInt() ?: 0
            )
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WeatherOverlay.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WeatherOverlay().apply {
                arguments = Bundle()
            }
    }
}