package com.example.signify_ch2_ps093.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.databinding.ItemDetailHomeBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDetailHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = 25

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

    }

    class ListViewHolder(var binding: ItemDetailHomeBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}