package in2000.pedalio.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import in2000.pedalio.R
import in2000.pedalio.utils.NetworkUtils
import kotlinx.coroutines.runBlocking


/**
 * The main screen of the app.
 */
class Title : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val networkAvailable = runBlocking {
            NetworkUtils.isNetworkAvailable()
        }

        return if (!networkAvailable) {
            // If no network is available, show the offline screen.
            findNavController().navigate(R.id.action_titleScreen_to_no_network_window)
            null
        } else {
            // If network is available, show the online (main) screen.
            val v = inflater.inflate(R.layout.fragment_title, container, false)
            val searchButton = v.findViewById<EditText>(R.id.search_button)

            // Set up the search button.
            searchButton.setOnClickListener {
                Navigation.findNavController(v).navigate(R.id.action_titleScreen_to_search_window)
            }
            v
        }

    }
}