package com.example.signify_ch2_ps093.ui.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.data.HomeModel
import com.example.signify_ch2_ps093.databinding.ItemDetailHomeBinding

class HomeArticleAdapter(private val itemList: ArrayList<HomeModel>) :
    RecyclerView.Adapter<HomeArticleAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemDetailHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    inner class ListViewHolder(private var binding: ItemDetailHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) {
            binding.tvTitleDetailHome.text = item.title
            binding.tvMessageDetailHome.text = item.message
            binding.ivPhoto.setImageResource(item.photo)
        }
    }
}