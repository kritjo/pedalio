package in2000.pedalio.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
                String.format("%.1f", it.temperature?: 0.0)
            )
            view.findViewById<TextView>(R.id.weather_wind).text = resources.getString(R.string.m_s,
                String.format("%.1f", it.windSpeed?: 0.0)
            )
            view.findViewById<TextView>(R.id.percipation).text = resources.getString(R.string.mm_h,
                String.format("%.1f", it.precipitation?: 0.0)
            )
            view.findViewById<ImageView>(R.id.weather_icon).setImageDrawable(resources.getDrawableForDensity(
                if (it.symbolCode == null) {
                    R.drawable.partlycloudy_day
                } else {
                resources.getIdentifier(
                    it.symbolCode,
                    "drawable",
                    activity?.packageName
                )},
                resources.displayMetrics.densityDpi,
                requireContext().theme
            ))

        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WeatherOverlay.
         */
        @JvmStatic
        fun newInstance() =
            WeatherOverlay().apply {
                arguments = Bundle()
            }
    }
}