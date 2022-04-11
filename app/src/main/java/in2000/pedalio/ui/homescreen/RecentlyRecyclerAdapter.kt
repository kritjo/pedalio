package in2000.pedalio.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import in2000.pedalio.R

class RecentlyRecyclerAdapter(val context: search_window, private val nylige: MutableList<String>):
    RecyclerView.Adapter<RecentlyRecyclerAdapter.Recently>() {

        class Recently(private val itemView: View) : RecyclerView.ViewHolder(itemView.rootView){
            val recent : TextView = itemView.findViewById(R.id.recentlySearch)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Recently {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recentlyitem, parent, false);
        return Recently(view)
    }

    override fun onBindViewHolder(holder: Recently, position: Int) {
        val text = nylige[position]
        holder.recent.text = text
    }

    override fun getItemCount(): Int {
        return nylige.size
    }
}