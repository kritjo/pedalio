package in2000.pedalio.ui.homescreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult

/**
 * Custom adapter for the recycler view in the home screen.
 */
class ResultAdapter(private val searchWindow: SearchWindow, private val searchList: List<SearchResult>, private val chosenResult: MutableLiveData<SearchResult>) :
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
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<Group>(R.id.searchResultClickGroup).setOnClickListener {
            chosenResult.postValue(searchList[position])
        }

        val current = searchList[position]
        viewHolder.itemView.findViewById<Button>(R.id.favButton).setOnClickListener {
            searchWindow.favoriteCallback(current.toFavorite())
        }
        val adr: String = when {
            current.address?.streetName == "" -> {
                current.address.municipality
            }
            current.address?.streetNumber ?: "" == "" -> {
                (current.address?.streetName ?: "") + ", " +
                        current.address?.municipality
            }
            else -> {
                (current.address?.streetName ?: "") + " " + (current.address?.streetNumber ?: "") + ", " +
                        current.address?.municipality
            }
        }
        if (current.poi != null && current.poi.name != "") {
            viewHolder.title.text = current.poi.name
            viewHolder.subTitle.text = adr
        } else {
            viewHolder.title.text = adr
            viewHolder.subTitle.text = ""
        }

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
