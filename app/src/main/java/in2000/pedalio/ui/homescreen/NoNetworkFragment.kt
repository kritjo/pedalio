package in2000.pedalio.ui.homescreen

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import in2000.pedalio.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Fragment that is shown when there is no internet connection on startup.
 */
class NoNetworkFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_no_network, container, false)

        // Register a network callback to listen for network changes.
        val connectivityManager = requireContext().getSystemService(ConnectivityManager::class.java)
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Must be launched on main thread
                lifecycleScope.launch(Dispatchers.Main) {
                    // Navigate to the main screen when the network is available.
                    Navigation.findNavController(v)
                        .navigate(R.id.action_no_network_to_titleScreen)
                }
                // Unregister the callback.
                connectivityManager.unregisterNetworkCallback(this)
            }
        }
        connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(),
                callback
            )
        return v
    }
}