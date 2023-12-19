package com.example.signify_ch2_ps093.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.signify_ch2_ps093.data.EssayAnswer
import com.example.signify_ch2_ps093.data.MultipleChoiceAnswer
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.FragmentQuizResultBinding
import com.example.signify_ch2_ps093.ui.camera.CameraActivity


class QuizResultFragment : Fragment() {

    private lateinit var binding: FragmentQuizResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val multipleChoiceAnswers = mutableMapOf<Int, MultipleChoiceAnswer>()
        val essayAnswers = mutableMapOf<Int, EssayAnswer>()

        for (questionIndex in 1..2) { // Ganti dengan jumlah pertanyaan Pilihan Ganda
            val answer = PilihanGandaFragment.UserPreferences.getMultipleChoiceAnswer(requireContext(), questionIndex)
            if (answer != null) {
                multipleChoiceAnswers[questionIndex] = answer
            }
        }

        // Mengambil jawaban untuk setiap pertanyaan pada Essay
        for (questionIndex in 1..2) { // Ganti dengan jumlah pertanyaan Essay
            val answer = EssayFragment.UserPreferences.getEssayAnswer(requireContext(), questionIndex)
            if (answer != null) {
                essayAnswers[questionIndex] = answer
            }
        }

        val savedPeragakanAnswer = CameraActivity.UserPreferences.getPeragakanAnswer(requireContext())

        // Tampilkan data yang diambil di QuizResultFragment
        // Contoh: Tampilkan hasil di Logd
        for ((index, answer) in multipleChoiceAnswers) {
            Log.d("QuizResultFragment", "Saved Multiple Choice Answer for question $index: $answer")
        }

        for ((index, answer) in essayAnswers) {
            Log.d("QuizResultFragment", "Saved Essay Answer for question $index: $answer")
        }

        Log.d("QuizResultFragment", "Saved Peragakan Answer: $savedPeragakanAnswer")

        val userLevel = UserPreference.getUserLevel(requireContext())
        val currentScore = 100
        binding.tvScore.text = currentScore.toString()

        binding.btnGoQuiz.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)

            if (currentScore == 100){

                val updateUserLevel = userLevel + 1
                UserPreference.setUserLevel(requireContext(), updateUserLevel)
            }

        }
    }
}