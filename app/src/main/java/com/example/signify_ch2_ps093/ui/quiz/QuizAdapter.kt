package com.example.signify_ch2_ps093.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ContentItem
import com.example.signify_ch2_ps093.data.network.QuizessItem

class QuizAdapter(
    private val userLevel: Int,
    private val onItemClickListener: OnContentItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val OPEN_TYPE = 1
    private val LOCKED_TYPE = 2

    private var quizLevels: List<QuizessItem> = emptyList()

    fun setData(levels: List<QuizessItem>) {
        this.quizLevels = levels
        notifyDataSetChanged()
    }

    fun getMaterialContents(): List<ContentItem> {
        return quizLevels.flatMap { it.content.orEmpty() }
            .filter { it.type == "material" }
    }

    fun getMaterialPilganContents(): List<ContentItem> {
        return quizLevels.flatMap { it.content.orEmpty() }
            .filter { it.type == "multiple_choices" }
    }

    fun getMaterialEssayContents(): List<ContentItem> {
        return quizLevels.flatMap { it.content.orEmpty() }
            .filter { it.type == "essay" }
    }

    fun getMaterialPeragakan(): List<ContentItem>{
        return quizLevels.flatMap { it.content.orEmpty() }
            .filter { it.type == "practice" }
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

            holder.itemView.setOnClickListener{
                levelItem.content?.let { contents ->
                    val materialContent = contents.find { it.type == "material" }
                    val pilganContent = contents.find { it.type == "multiple_choices" }
                    val essayContent = contents.find { it.type == "essay" }
                    val peragakanContent = contents.find { it.type == "practice" }


                    if (materialContent != null) {
                        onItemClickListener.onContentItemClicked(materialContent)
                    } else if (pilganContent != null) {
                        onItemClickListener.onContentItemClicked(pilganContent)
                    } else if (essayContent != null){
                        onItemClickListener.onContentItemClicked(essayContent)
                    } else if (peragakanContent != null){
                        onItemClickListener.onContentItemClicked(peragakanContent)
                    }
                }
            }

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
        private val kosaKata: TextView = itemView.findViewById(R.id.tv_subtitle_level)
        private val imgOpen: ImageView = itemView.findViewById(R.id.iv_image_level_open)

        fun bindOpen(quizItem: QuizessItem) {
            levelNameTextView.text = quizItem.levelName
            kosaKata.text = quizItem.totals
            Glide.with(itemView.context)
                .load(quizItem.imgOpen)
                .centerCrop()
                .into(imgOpen)
            // Setup tampilan jika item terbuka

        }
    }

    inner class LockedQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val levelNameTextView: TextView = itemView.findViewById(R.id.tv_title_level)
        private val kosaKata: TextView = itemView.findViewById(R.id.tv_subtitle_level)
        private val imgLocked: ImageView = itemView.findViewById(R.id.iv_image_level_locked)

        fun bindLocked(quizItem: QuizessItem) {
            levelNameTextView.text = quizItem.levelName
            kosaKata.text = quizItem.totals
            Glide.with(itemView.context)
                .load(quizItem.imgLocked)
                .centerCrop()
                .into(imgLocked)
            // Setup tampilan jika item terkunci
        }
    }

    interface OnContentItemClickListener {
        fun onContentItemClicked(contentItem: ContentItem)
    }
}
