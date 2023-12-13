package com.example.signify_ch2_ps093.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.QuizessItem

class QuizAdapter(
    private val userLevel: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val OPEN_TYPE = 1
    private val LOCKED_TYPE = 2

    private var quizLevels: List<QuizessItem> = emptyList()

    fun setData(levels: List<QuizessItem>) {
        this.quizLevels = levels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == OPEN_TYPE) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemView = layoutInflater.inflate(R.layout.item_level, parent, false)
            OpenQuizViewHolder(itemView)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemView = layoutInflater.inflate(R.layout.item_level_lock, parent, false)
            LockedQuizViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val levelItem = quizLevels[position]
        if (holder is OpenQuizViewHolder) {
            holder.bindOpen(levelItem)
        } else if (holder is LockedQuizViewHolder) {
            holder.bindLocked(levelItem)
        }
    }

    override fun getItemCount(): Int {
        return quizLevels.size
    }

    override fun getItemViewType(position: Int): Int {
        val levelItem = quizLevels[position]
        return if (levelItem.level!! <= userLevel) {
            OPEN_TYPE
        } else {
            LOCKED_TYPE
        }
    }

    inner class OpenQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val levelNameTextView: TextView = itemView.findViewById(R.id.tv_title_level)

        fun bindOpen(quizItem: QuizessItem) {
            levelNameTextView.text = quizItem.levelName
            // Setup tampilan jika item terbuka
        }
    }

    inner class LockedQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val levelNameTextView: TextView = itemView.findViewById(R.id.tv_title_level)

        fun bindLocked(quizItem: QuizessItem) {
            levelNameTextView.text = quizItem.levelName
            // Setup tampilan jika item terkunci
        }
    }
}
