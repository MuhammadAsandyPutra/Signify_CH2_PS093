package com.example.signify_ch2_ps093.ui.quiz

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.data.LevelItem
import com.example.signify_ch2_ps093.databinding.ItemLevelBinding
import com.example.signify_ch2_ps093.databinding.ItemLevelLockBinding


class LevelAdapter(private val levelList: List<LevelItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_UNLOCKED = 1
        private const val VIEW_TYPE_LOCKED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_UNLOCKED -> {
                val binding = ItemLevelBinding.inflate(inflater, parent, false)
                UnlockedLevelViewHolder(binding)
            }

            VIEW_TYPE_LOCKED -> {
                val binding = ItemLevelLockBinding.inflate(inflater, parent, false)
                LockedLevelViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = levelList[position]

        when (holder) {
            is UnlockedLevelViewHolder -> {
                holder.bind(item)
                holder.itemView.setOnClickListener {
                    navigateToQuizDetailActivity(item.level, holder.itemView.context)
                }
            }

            is LockedLevelViewHolder -> {
                holder.bind(item)
                holder.itemView.setOnClickListener {
                    showLockedToast(item.level, holder.itemView.context)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (levelList[position].isUnlocked) VIEW_TYPE_UNLOCKED else VIEW_TYPE_LOCKED
    }

    inner class UnlockedLevelViewHolder(private val binding: ItemLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LevelItem) {
            binding.tvNoLevel.text = item.level.toString()
            binding.tvTitleLevel.text = getTitleForLevel(item.level)
        }

        private fun getTitleForLevel(level: Int): String {
            return "Level $level"
        }
    }

    // ViewHolder for locked level item
    inner class LockedLevelViewHolder(private val binding: ItemLevelLockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LevelItem) {
            binding.tvNoLevel.text = item.level.toString()
            binding.tvTitleLevel.text = getTitleForLevel(item.level)
        }

        private fun getTitleForLevel(level: Int): String {
            return "Level $level"
        }
    }

    private fun navigateToQuizDetailActivity(level: Int, context: Context) {
        val intent = Intent(context, QuizDetailActivity::class.java)
        intent.putExtra("level", level)
        context.startActivity(intent)
    }

    private fun showLockedToast(level: Int, context: Context) {
        val toastMessage = "Level $level terkunci"
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
}

