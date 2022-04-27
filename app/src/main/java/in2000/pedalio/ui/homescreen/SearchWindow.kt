package in2000.pedalio.ui.homescreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.data.search.impl.FuzzySearchRepository
import in2000.pedalio.data.search.source.FuzzySearchSource
import in2000.pedalio.data.settings.impl.SharedPreferences
import in2000.pedalio.viewmodel.MapViewModel
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors.newSingleThreadExecutor


/**
 * The search window fragment. This fragment is responsible for displaying the search window on
 * click of the search button from the map.
 */
class SearchWindow : Fragment() {
    private val mapViewModel: MapViewModel by activityViewModels()
    private val chosenResult = MutableLiveData<SearchResult>()

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
        val sharedPreferences = SharedPreferences(requireContext())
        favoriteRecycler.adapter =
            FavoriteRecyclerAdapter(this, sharedPreferences.favoriteSearches, chosenResult)

        val results = MutableLiveData(emptyList<SearchResult>())
        val recent = sharedPreferences.recentSearches

        // Add a listener for when "done" key is pressed on keyboard
        search.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE
                || i == EditorInfo.IME_ACTION_NEXT
            ) {
                getSystemService(requireContext(), InputMethodManager::class.java)
                    ?.hideSoftInputFromWindow(requireView().windowToken, 0)
                true
            } else {
                false
            }
        }

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
            hideProgressBar()
            // Only null if there is an error
            if (it == null) {
                Toast.makeText(
                    requireContext(),
                    "Ingen resultater. Sjekk nettverksforbinnelsen.", // TODO: this should be a text resource
                    Toast.LENGTH_SHORT
                ).show()

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
            showProgressBar()
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
                var result = FuzzySearchRepository(requireContext())
                    .doSearch(
                        FuzzySearchSource()
                            .createSpecification(
                                it.toString(),
                                mapViewModel.currentPos().value!!
                            )
                    )
                // do search again with larger radius from oslo if there is no result.
                if (result == null || result.isEmpty()) {
                    result = FuzzySearchRepository(requireContext())
                        .doSearch(
                            FuzzySearchSource()
                                .createSpecification(
                                    it.toString(),
                                    LatLng(59.914831510617894, 10.732526176708147),
                                    40000.0 // 120 km
                                )
                        )
                }
                stateRecently.postValue(false)
                results.postValue(result)
            }
        }

        // Back button.
        search.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (target == DrawablePosition.LEFT)
                    Navigation.findNavController(v)
                        .navigate(R.id.action_search_window_to_titleScreen)
            }
        })

        // If user chooses a search result, navigate to the map and pass the result.
        chosenResult.observe(viewLifecycleOwner) {
            if (it != null) {
                sharedPreferences.appendRecentSearch(it)
                mapViewModel.chosenSearchResult.postValue(it)
                // clear prev route, as we are navigating to a new place.
                mapViewModel.chosenRoute.postValue(null)
                mapViewModel.newSearchResult = true
                Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
            }
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Show the keyboard.
        val search = requireView().findViewById<EditText>(R.id.search)
        search.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT)

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

    private fun showProgressBar() {
        requireView().findViewById<ProgressBar>(R.id.search_progressBar).visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        requireView().findViewById<ProgressBar>(R.id.search_progressBar).visibility = View.GONE
    }
}