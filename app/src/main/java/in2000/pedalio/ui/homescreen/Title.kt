package in2000.pedalio.ui.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import in2000.pedalio.R
import in2000.pedalio.utils.NetworkUtils
import kotlinx.coroutines.runBlocking


/**
 * A simple [Fragment] subclass.
 * Use the [Title.newInstance] factory method to
 * create an instance of this fragment.
 */
class Title : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val networkAvailable = runBlocking {
            NetworkUtils.isNetworkAvailable()
        }

        return if (!networkAvailable) {
            findNavController().navigate(R.id.action_titleScreen_to_no_network_window)
            null
        } else {
            val v = inflater.inflate(R.layout.fragment_title, container, false)
            val searchButton = v.findViewById<EditText>(R.id.search_button)
            searchButton.setOnClickListener{
                Navigation.findNavController(v).navigate(R.id.action_titleScreen_to_search_window)}
            // Inflate the layout for this fragment
            v
        }

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Title.
         */
        @JvmStatic
        fun newInstance() = Title()
    }
}