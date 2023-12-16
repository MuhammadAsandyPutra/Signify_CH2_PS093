package com.example.signify_ch2_ps093.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.data.HomeModel
import com.example.signify_ch2_ps093.databinding.ItemDetailHomeBinding
import com.example.signify_ch2_ps093.ui.materigrup.MateriGrupActivity
import com.example.signify_ch2_ps093.ui.quiz.QuizActivity

class HomeAdapter(private val itemList: ArrayList<HomeModel>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemDetailHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = if (position == 0) {
                Intent(context, MateriGrupActivity::class.java)
            } else {
                Intent(context, QuizActivity::class.java)
            }
            
            val options = ActivityOptions.makeCustomAnimation(context, 0, 0)
            context.startActivity(intent, options.toBundle())
        }
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