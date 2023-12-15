package com.example.signify_ch2_ps093.ui.materigrup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ListItem
import com.example.signify_ch2_ps093.data.network.MaterialsItem

class CategoryAdapter(private val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(materialsItem: MaterialsItem)
    }

    private var listener: OnItemClickListener? = null
    private var categoryItemsMap: Map<String, List<ListItem>> = mapOf()
    private var categories: List<MaterialsItem?> = listOf()

    fun setCategoryItemsMap(map: Map<String, List<ListItem>>) {
        this.categoryItemsMap = map
        notifyDataSetChanged()
    }

    fun setData(categories: List<MaterialsItem?>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    fun getCategoryItemsMap(): Map<String, List<ListItem>> {
        return categoryItemsMap
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_materi, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)

    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.tv_title)
        private var currentItem: MaterialsItem? = null
        private val kosaKata: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val imgCategory: ImageView = itemView.findViewById(R.id.iv_image)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = categories[position]
                    clickedItem?.let {
                        listener?.onItemClick(it)
                    }
                }
            }
        }

        fun bind(category: MaterialsItem?) {
            currentItem = category
            categoryName.text = category?.category
            kosaKata.text = category?.totals
            Glide.with(itemView.context)
                .load(category?.img)
                .centerCrop()
                .into(imgCategory)
        }
    }
}
