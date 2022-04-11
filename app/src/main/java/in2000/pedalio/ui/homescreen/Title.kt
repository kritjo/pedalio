package in2000.pedalio.ui.homescreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import in2000.pedalio.R


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
        val v = inflater.inflate(R.layout.fragment_title, container, false)


        val search_button = v.findViewById<EditText>(R.id.search_button)
        search_button.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_titleScreen_to_search_window)}
        // Inflate the layout for this fragment
        return v
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