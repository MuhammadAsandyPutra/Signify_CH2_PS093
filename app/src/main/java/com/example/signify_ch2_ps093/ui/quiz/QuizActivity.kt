package com.example.signify_ch2_ps093.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.signify_ch2_ps093.data.network.ApiConfig
import com.example.signify_ch2_ps093.data.network.ContentItem
import com.example.signify_ch2_ps093.data.network.QuizResponse
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityQuizBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity(), QuizAdapter.OnContentItemClickListener {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userHighestLevel = UserPreference.getUserLevel(this)
        val usernames = UserPreference.getUsername(this)
        binding.usernameHeader.text = "Let's go, $usernames"

        binding.rvLevel
        binding.rvLevel.layoutManager = LinearLayoutManager(this)
        quizAdapter = QuizAdapter(userHighestLevel, this)
        binding.rvLevel.adapter = quizAdapter

        fetchData()

    }

    override fun onContentItemClicked(contentItem: ContentItem) {
        if (contentItem.type == "material"){
            val materialContents = quizAdapter.getMaterialContents()
            val pilganContents = quizAdapter.getMaterialPilganContents()
            val essayContent = quizAdapter.getMaterialEssayContents()
            val peragakanContent = quizAdapter.getMaterialPeragakan()

            val intentDetail = Intent(this@QuizActivity, QuizDetailActivity::class.java).apply {
                val multipleArrayList = ArrayList(pilganContents)
                val materialArrayList = ArrayList(materialContents)
                val essayArrayList = ArrayList(essayContent)
                val peragakanArrayList = ArrayList(peragakanContent)

                putParcelableArrayListExtra("MATERIAL", materialArrayList)
                putParcelableArrayListExtra("MULTIPLE_CHOICES", multipleArrayList)
                putParcelableArrayListExtra("ESSAY", essayArrayList)
                putParcelableArrayListExtra("PRACTICE", peragakanArrayList )
            }
            startActivity(intentDetail)
        }
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

