package in2000.pedalio.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import in2000.pedalio.viewmodel.MapViewModel
import kotlinx.coroutines.*
import java.util.concurrent.Executors.newSingleThreadExecutor

/**
 * A simple [Fragment] subclass.
 * Use the [search_window.newInstance] factory method to
 * create an instance of this fragment.
 */
class search_window : Fragment() {

    private val mapViewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search_window, container, false)
        val search = v.findViewById<com.mindorks.editdrawabletext.EditDrawableText>(R.id.search)
        val recyclerView = v.findViewById<RecyclerView>(R.id.recycler1)
        val liveData = MutableLiveData(emptyList<SearchResult>())
        liveData.observe(viewLifecycleOwner) {
            recyclerView.adapter = CustomAdapter(it)
        }
        liveData.postValue(emptyList())

        val coroutineDispatcher = newSingleThreadExecutor().asCoroutineDispatcher()
        var timeLastSearch = System.currentTimeMillis()

        search.addTextChangedListener {
            lifecycleScope.launch(coroutineDispatcher) {
                 if (System.currentTimeMillis() - timeLastSearch < 250) {
                     delay(250 - (System.currentTimeMillis() - timeLastSearch))
                 }
                 timeLastSearch = System.currentTimeMillis()
                if (it == null) {
                    liveData.postValue(emptyList())
                    return@launch
                }
                if (it.isBlank() || it.isEmpty()) {
                    liveData.postValue(emptyList())
                    return@launch
                }
                 val result = FuzzySearchRepository(requireContext())
                    .doSearch(
                        FuzzySearchSource()
                            .createSpecification(
                                it.toString(),
                                mapViewModel.currentPos.value!!
                            )
                    )
                liveData.postValue(result)
            }
        }

        search.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                Navigation.findNavController(v).navigate(R.id.action_search_window_to_titleScreen)
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