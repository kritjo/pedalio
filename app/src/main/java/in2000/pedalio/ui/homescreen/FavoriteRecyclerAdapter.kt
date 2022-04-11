package in2000.pedalio.ui.homescreen

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import in2000.pedalio.R

class FavoriteRecyclerAdapter(val context: search_window, private val addresses: MutableList<Address>, private val mOnNoteListener: OnNoteListener): RecyclerView.Adapter<FavoriteRecyclerAdapter.Favorite>(){

    lateinit var item: View

    class Favorite(private val itemView: View, private val onNoteListener: OnNoteListener) : RecyclerView.ViewHolder(itemView.rootView), View.OnClickListener{ // Undersook om jeg trenger aa bruke root her
        val address : TextView = itemView.findViewById(R.id.adressen)
        val addrImg : ImageView = itemView.findViewById(R.id.adreseImg)
        val favoriteBtn : CardView = itemView.findViewById(R.id.favorite_button)



        /*val listener = itemView.setOnClickListener(this)*/

        override fun onClick(p0: View?) {
            onNoteListener.onNoteClick(adapterPosition);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favorite {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_favorititem, parent, false);
        return Favorite(view, mOnNoteListener)
    }

    override fun onBindViewHolder(holder: Favorite, position: Int) {
        val drawRec = mutableListOf(R.drawable.ic_home, R.drawable.ic_action_work, R.drawable.ic_action_add)
        val text = addresses[position].address
        holder.address.text = text
        val src = addresses[position].imgSrc
        holder.addrImg.setImageResource(drawRec[src])
        holder.favoriteBtn.setOnClickListener{
            Log.d("Cliked", position.toString())
            if (text == "Legg til"){
                Log.d("Legg Til", position.toString())
            }
        }

    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    interface OnNoteListener{
        fun onNoteClick(position: Int)
    }



}

