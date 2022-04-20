package in2000.pedalio.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.data.search.impl.FuzzySearchRepository
import in2000.pedalio.data.search.source.FuzzySearchSource
import in2000.pedalio.data.settings.impl.SharedPreferences
import in2000.pedalio.viewmodel.MapViewModel
import kotlinx.coroutines.*
import java.util.concurrent.Executors.newSingleThreadExecutor

/**
 * A simple [Fragment] subclass.
 * Use the [SearchWindow.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchWindow : Fragment() {

    private val mapViewModel: MapViewModel by activityViewModels()
    private val chosenResult = MutableLiveData<SearchResult>()

    /**
     * The search result list.
     * @see SearchResult
     * Will be updated when the user types in the search bar.
     * Threds are used to avoid blocking the UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search_window, container, false)

        val stateRecently = MutableLiveData(true)
        val lowerRecyclerHint = v.findViewById<TextView>(R.id.lowerRecyclerHint)
        val search = v.findViewById<com.mindorks.editdrawabletext.EditDrawableText>(R.id.search)
        val favoriteRecycler = v.findViewById<RecyclerView>(R.id.search_favorites)
        val sharedPreferences = SharedPreferences(requireContext())
        favoriteRecycler.adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)

        val recyclerView = v.findViewById<RecyclerView>(R.id.lowerRecycler)

        val results = MutableLiveData(emptyList<SearchResult>())
        val recents = sharedPreferences.recentSearches

        stateRecently.observe(viewLifecycleOwner) {
            if (it) {
                lowerRecyclerHint.text = getString(R.string.recently_searched_hint)
                val adapter = ResultAdapter(this, recents, chosenResult)
                recyclerView.adapter = adapter
            } else {
                lowerRecyclerHint.text = getString(R.string.search_result_hint)
            }
        }
        results.observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(requireContext(), "Ingen resultater. Sjekk nettverksforbinnelsen.", Toast.LENGTH_SHORT).show()

            } else {
                if (stateRecently.value == false) {
                    val adapter = ResultAdapter(this, it, chosenResult)
                    recyclerView.adapter = adapter
                }
            }
        }
        results.postValue(emptyList())

        val coroutineDispatcher = newSingleThreadExecutor().asCoroutineDispatcher()
        var timeLastSearch = System.currentTimeMillis()


        /**
         * The search bar listener.
         * threading is done using coroutines.
         * @see coroutineDispatcher
         */
        search.addTextChangedListener {
            lifecycleScope.launch(coroutineDispatcher) {
                 if (System.currentTimeMillis() - timeLastSearch < 500) {
                     delay(500 - (System.currentTimeMillis() - timeLastSearch))
                 }
                 timeLastSearch = System.currentTimeMillis()
                if (it == null) {
                    results.postValue(emptyList())
                    stateRecently.postValue(true)
                    return@launch
                }
                if (it.isBlank() || it.isEmpty()) {
                    results.postValue(emptyList())
                    stateRecently.postValue(true)
                    return@launch
                }
                 val result = FuzzySearchRepository(requireContext())
                    .doSearch(
                        FuzzySearchSource()
                            .createSpecification(
                                it.toString(),
                                mapViewModel.currentPos().value!!
                            )
                    )
                stateRecently.postValue(false)
                results.postValue(result)
            }
        }

        search.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (target == DrawablePosition.LEFT)
                    Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
            }
        })

        chosenResult.observe(viewLifecycleOwner) {
            if (it != null) {
                sharedPreferences.appendRecentSearch(it)
                mapViewModel.chosenSearchResult.postValue(it)
                Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
            }
        }

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
            SearchWindow().apply {
                arguments = Bundle()
            }
    }

    fun favoriteCallback(current: FavoriteResult) {
        val sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.appendFavoriteSearch(current)
        requireView().findViewById<RecyclerView>(R.id.search_favorites).adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)
    }

    fun favoriteRemoveCallback(current: FavoriteResult) {
        val sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.removeFavorite(current)
        requireView().findViewById<RecyclerView>(R.id.search_favorites).adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)
    }
}