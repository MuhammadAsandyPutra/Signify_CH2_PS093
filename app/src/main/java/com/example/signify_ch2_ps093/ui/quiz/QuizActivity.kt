package com.example.signify_ch2_ps093.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.signify_ch2_ps093.data.network.ApiConfig
import com.example.signify_ch2_ps093.data.network.QuizResponse
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityQuizBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
//    private lateinit var levelAdapter: LevelAdapter
//    private val levelList = mutableListOf<LevelItem>()

    private lateinit var quizAdapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userHighestLevel = UserPreference.getUserLevel(this)
//
//        for (i in 1..5) {
//            val isUnlocked = i <= userHighestLevel
//            levelList.add(LevelItem(i, isUnlocked))
//        }
//
//        levelAdapter = LevelAdapter(levelList)
//        binding.rvLevel.layoutManager = LinearLayoutManager(this)
//        binding.rvLevel.adapter = levelAdapter
        binding.rvLevel
        binding.rvLevel.layoutManager = LinearLayoutManager(this)

        quizAdapter = QuizAdapter(userHighestLevel) {materialContent ->
            val name = materialContent.name
            val link = materialContent.link

            val intent = Intent(this@QuizActivity, QuizDetailActivity::class.java).apply {
                putExtra("NAME", name)
                putExtra("LINK", link)
            }

//            val intentBundle = Intent(this@QuizActivity, QuizDetailActivity::class.java).apply {
//                val bundle = Bundle().apply {
//                    putParcelableArrayListExtra("MATERIALS", ArrayList(materialContent))
//                }
//                putExtras(bundle)
//
//
//            }
            startActivity(intent)

        }
        binding.rvLevel.adapter = quizAdapter

        fetchData()

    }

    private fun fetchData() {
        val apiService = ApiConfig.getApiService()
        apiService.getQuiz().enqueue(object : Callback<QuizResponse> {
            override fun onResponse(
                call: Call<QuizResponse>,
                response: Response<QuizResponse>
            ) {
                if (response.isSuccessful) {
                    val quizResponse = response.body()
                    quizResponse?.quizess?.let { quizLevels ->

                        quizAdapter.setData(quizLevels)
                    }
                }
            }

            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}




//            val name = materialContent.name
//            val link = materialContent.link
//
//            val intent = Intent(this@QuizActivity, QuizDetailActivity::class.java).apply {
//                putExtra("NAME", name)
//                putExtra("LINK", link)
//            }