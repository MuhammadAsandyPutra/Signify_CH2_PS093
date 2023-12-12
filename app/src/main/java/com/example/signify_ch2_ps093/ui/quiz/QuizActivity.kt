package com.example.signify_ch2_ps093.ui.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.signify_ch2_ps093.data.LevelItem
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var levelAdapter: LevelAdapter
    private val levelList = mutableListOf<LevelItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userHighestLevel = UserPreference.getUserLevel(this)

        for (i in 1..5) {
            val isUnlocked = i <= userHighestLevel
            levelList.add(LevelItem(i, isUnlocked))
        }

        levelAdapter = LevelAdapter(levelList)
        binding.rvLevel.layoutManager = LinearLayoutManager(this)
        binding.rvLevel.adapter = levelAdapter
    }
}
