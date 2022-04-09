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
import androidx.core.content.getSystemService
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import in2000.pedalio.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [search_window.newInstance] factory method to
 * create an instance of this fragment.
 */
class search_window : Fragment(), FavoriteRecyclerAdapter.OnNoteListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val adresser = mutableListOf("Gaustadalléen 23B", "Ravnkollbakken 37", "+")
    val nylige = mutableListOf("Blindgaten 34", "Gateveien 2", "Stedgaten 22b", "Ingenmannsland 98")

    val fav = mutableListOf(Adresse("Tulleveien 23B", 0),Adresse("Gaustadalléen 23B", 0),Adresse("Ravnkollbakken 37", 1),  Adresse("Legg til", 2))


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
        val v = inflater.inflate(R.layout.fragment_search_window, container, false)

        val viewet = v.findViewById<EditText>(R.id.search)
        val con = context
        if (viewet != null && con != null){
            showSoftKeyboard(viewet, con)
        }

        viewet.requestFocus()

        var recyclerView = v.findViewById<RecyclerView>(R.id.favorit_recyclerView)
        val adapter = FavoriteRecyclerAdapter(this, fav, this)
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        var recyclerView2 = v.findViewById<RecyclerView>(R.id.recently_recyclerView)
        val adapter2 = RecentlyRecyclerAdapter(this, nylige)
        var linearLayoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView2.layoutManager = linearLayoutManager2
        recyclerView2.adapter = adapter2


        val search = v.findViewById<com.mindorks.editdrawabletext.EditDrawableText>(R.id.search)
        search.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                when (target) {
                        DrawablePosition.LEFT -> Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
                }
            }
        })
        return v
    }

    fun showSoftKeyboard(view: View, context: Context) {
        if (view.requestFocus()) {
            Log.d("kom", "her")
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment search_window.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            search_window().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onNoteClick(position: Int) {
        Log.d("oneNoteClick", position.toString())
    }
}