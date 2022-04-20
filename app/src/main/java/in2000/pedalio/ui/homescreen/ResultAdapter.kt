package in2000.pedalio.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult

/**
 * Recycler for the search result list inside of the search window
 *
 * @property searchWindow
 * @property searchList The list of search results
 * @property chosenResult A MutableLiveData that is used to trigger a callback if a favorite is
 * clicked
 */
class ResultAdapter(
    private val searchWindow: SearchWindow,
    private val searchList: List<SearchResult>,
    private val chosenResult: MutableLiveData<SearchResult>) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.resultTitle)
        val subTitle: TextView = view.findViewById(R.id.resultSubTitle)
        val kmToResult: TextView = view.findViewById(R.id.resultUnderLocationText)
    }

    /**
     * Create new views (invoked by the layout manager).
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_card, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager).
     */

    //TODO: replace toString() with the correct structure
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<ConstraintLayout>(R.id.searchResultClickGroup).setOnClickListener {
            chosenResult.postValue(searchList[position])
        }

        val current = searchList[position]
        viewHolder.itemView.findViewById<Button>(R.id.favButton).setOnClickListener {
            searchWindow.addFavorite(current.toFavorite())
        }

        // The adress is set to Street + Number, City. Unless some are empty
        val adr: String = when {
            // If the street is empty, add the municipality
            current.address?.streetName == "" -> {
                current.address.municipality
            }

            // If the number is empty, add the street and municipality
            current.address?.streetNumber ?: "" == "" -> {
                (current.address?.streetName ?: "") + ", " +
                        current.address?.municipality
            }
            // Otherwise add all
            else -> {
                (current.address?.streetName ?: "") + " " + (current.address?.streetNumber ?: "") + ", " +
                        current.address?.municipality
            }
        }

        // If we have a poi, set it as the title and the address as the subtitle
        if (current.poi != null && current.poi.name != "") {
            viewHolder.title.text = current.poi.name
            viewHolder.subTitle.text = adr
        } else {
            // Otherwise set the title to the address and no subtitle
            viewHolder.title.text = adr
            viewHolder.subTitle.text = ""
        }

        // Add KM with one decimal precision
        viewHolder.kmToResult.text =
            if (current.distance > 1000)
                    String.format("%.1f", (current.distance / 1000)) + " km"
            else current.distance.toInt().toString() + " m"
    }

    /**
     * Return the size of your dataset (invoked by the layout manager).
     */
    override fun getItemCount() = searchList.size

}

private fun SearchResult.toFavorite() = FavoriteResult(
    score = score,
    address = address,
    position = position,
    distance = distance,
    info = info,
    poi = poi,
)
