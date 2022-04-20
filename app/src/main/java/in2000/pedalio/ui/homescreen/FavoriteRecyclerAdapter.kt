package in2000.pedalio.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult

/**
 * Recycler for the favorite list inside of the search window
 *
 * @property searchWindow
 * @property favorites
 * @property chosenResult A MutableLiveData that is used to trigger a callback if a favorite is
 * clicked
 */
class FavoriteRecyclerAdapter(
    private val searchWindow: SearchWindow,
    private val favorites: List<FavoriteResult>,
    private val chosenResult: MutableLiveData<SearchResult>
): RecyclerView.Adapter<FavoriteRecyclerAdapter.Favorite>(){

    class Favorite(view: View) : RecyclerView.ViewHolder(view){
        val address : TextView = itemView.findViewById(R.id.favText)
        val addrImg : ImageView = itemView.findViewById(R.id.favIco)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favorite {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_favorititem, parent, false)
        return Favorite(view)
    }

    override fun onBindViewHolder(holder: Favorite, position: Int) {
        val current = favorites[position]
        holder.itemView.findViewById<MaterialCardView>(R.id.favorite_button).setOnClickListener {
            chosenResult.postValue(current.toSearchResult())
        }
        holder.itemView.findViewById<MaterialCardView>(R.id.favorite_button).setOnLongClickListener {
            searchWindow.removeFavorite(current)
            true
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
        // If we have a poi name, set it as the text
        if (current.poi != null && current.poi.name != "") {
            holder.address.text = current.poi.name
        } else {
            // Otherwise set the address
            holder.address.text = adr
        }

        // Set the icon
        holder.addrImg.setImageResource(current.iconSrc)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

}