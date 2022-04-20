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
 * The search window fragment. This fragment is responsible for displaying the search window on
 * click of the search button from the map.
 */
class SearchWindow : Fragment() {
    private val mapViewModel: MapViewModel by activityViewModels()
    private val chosenResult = MutableLiveData<SearchResult>()
    private val sharedPreferences = SharedPreferences(requireContext())

    // Using a single thread executor to avoid race conditions.
    private val coroutineDispatcher = newSingleThreadExecutor().asCoroutineDispatcher()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search_window, container, false)

        // state of the search window. If true, we should display recently searched results.
        // If false, we should display the search results.
        // Should be true only when search input is empty.
        val stateRecently = MutableLiveData(true)

        val recyclerView = v.findViewById<RecyclerView>(R.id.lowerRecycler)
        val lowerRecyclerHint = v.findViewById<TextView>(R.id.lowerRecyclerHint)
        val search = v.findViewById<com.mindorks.editdrawabletext.EditDrawableText>(R.id.search)
        val favoriteRecycler = v.findViewById<RecyclerView>(R.id.search_favorites)

        favoriteRecycler.adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)

        val results = MutableLiveData(emptyList<SearchResult>())
        val recent = sharedPreferences.recentSearches

        stateRecently.observe(viewLifecycleOwner) {
            if (it) {
                // Display recently searched results.
                lowerRecyclerHint.text = getString(R.string.recently_searched_hint)
                val adapter = ResultAdapter(this, recent, chosenResult)
                recyclerView.adapter = adapter
            } else {
                lowerRecyclerHint.text = getString(R.string.search_result_hint)
            }
        }

        results.observe(viewLifecycleOwner) {
            // Only null if there is an error
            if (it == null) {
                Toast.makeText(requireContext(), "Ingen resultater. Sjekk nettverksforbinnelsen.", Toast.LENGTH_SHORT).show()

            } else {
                if (stateRecently.value == false) {
                    // Display search results.
                    val adapter = ResultAdapter(this, it, chosenResult)
                    recyclerView.adapter = adapter
                }
            }
        }
        // Initialize the search results.
        results.postValue(emptyList())

        // Only search every 500ms.
        var timeLastSearch = System.currentTimeMillis()
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

        // Back button.
        search.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (target == DrawablePosition.LEFT)
                    Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
            }
        })

        // If user chooses a search result, navigate to the map and pass the result.
        chosenResult.observe(viewLifecycleOwner) {
            if (it != null) {
                sharedPreferences.appendRecentSearch(it)
                mapViewModel.chosenSearchResult.postValue(it)
                Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
            }
        }

        return v
    }

    /**
     * Used to add a favorite to the database.
     *
     * @param current the favorite to be added.
     */
    fun addFavorite(current: FavoriteResult) {
        val sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.appendFavoriteSearch(current)
        requireView().findViewById<RecyclerView>(R.id.search_favorites).adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)
    }


    /**
     * Used to remove a favorite from the database.
     *
     * @param current the favorite to be removed.
     */
    fun removeFavorite(current: FavoriteResult) {
        val sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.removeFavorite(current)
        requireView().findViewById<RecyclerView>(R.id.search_favorites).adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)
    }
}