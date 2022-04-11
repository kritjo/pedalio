package in2000.pedalio.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import in2000.pedalio.R
import in2000.pedalio.data.search.SearchResult


class FavoriteRecyclerAdapter(val searchWindow: SearchWindow,
                              private val favorites: List<FavoriteResult>,
                              private val chosenResult: MutableLiveData<SearchResult>
): RecyclerView.Adapter<FavoriteRecyclerAdapter.Favorite>(){
    lateinit var item: View

    class Favorite(view: View) : RecyclerView.ViewHolder(view){
        val address : TextView = itemView.findViewById(R.id.favText)
        val addrImg : ImageView = itemView.findViewById(R.id.favIco)
        val favoriteBtn : CardView = itemView.findViewById(R.id.favorite_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favorite {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_favorititem, parent, false);
        return Favorite(view)
    }

    override fun onBindViewHolder(holder: Favorite, position: Int) {
        val current = favorites[position]
        holder.itemView.findViewById<MaterialCardView>(R.id.favorite_button).setOnClickListener {
            chosenResult.postValue(current.toSearchResult())
        }
        holder.itemView.findViewById<MaterialCardView>(R.id.favorite_button).setOnLongClickListener {
            searchWindow.favoriteRemoveCallback(current)
            true
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
            holder.address.text = current.poi.name
        } else {
            holder.address.text = adr
        }
        holder.addrImg.setImageResource(current.iconSrc)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

}