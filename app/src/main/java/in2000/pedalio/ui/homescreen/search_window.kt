package in2000.pedalio.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import in2000.pedalio.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [search_window.newInstance] factory method to
 * create an instance of this fragment.
 */
class search_window : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search_window, container, false)
        val search = v.findViewById<com.mindorks.editdrawabletext.EditDrawableText>(R.id.search)

        val list = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        v.findViewById<RecyclerView>(R.id.recycler1).adapter = CustomAdapter(list)

        search.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                when (target) {
                        DrawablePosition.LEFT -> Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
                }
            }
        })
        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment search_window.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            search_window().apply {
                arguments = Bundle()
            }
    }
}