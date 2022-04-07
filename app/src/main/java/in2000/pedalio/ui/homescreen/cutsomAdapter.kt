package in2000.pedalio.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult

/**
 * Custom adapter for the recycler view in the home screen.
 */
class CustomAdapter(val searchList: List<SearchResult>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.testTextView)
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
        viewHolder.textView.text = searchList[position].toString()
    }

    /**
     * Return the size of your dataset (invoked by the layout manager).
     */
    override fun getItemCount() = searchList.size

}