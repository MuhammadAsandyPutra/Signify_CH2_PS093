package com.example.signify_ch2_ps093.ui.materigrup

// DetailItemAdapter.kt
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ListItem

class DetailItemAdapter(private val items: List<ListItem>) : RecyclerView.Adapter<DetailItemAdapter.ViewHolder>() {

    private var filteredItems: List<ListItem> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_item_materi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredItems[position]
        holder.nameTextView.text = currentItem.name

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailMateriActivity::class.java)
            intent.putExtra("SELECTED_LINK", currentItem.link)
            intent.putExtra("SELECTED_TITLE", currentItem.name)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tv_title)

    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter { it.name?.contains(query, ignoreCase = true) ?: false }
        }
        notifyDataSetChanged()
    }

}
