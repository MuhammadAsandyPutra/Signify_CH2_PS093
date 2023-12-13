package com.example.signify_ch2_ps093.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.QuestionAnswer
import com.example.signify_ch2_ps093.databinding.FragmentPilihanGandaBinding

class PilihanGandaFragment : Fragment() {

    private var _binding : FragmentPilihanGandaBinding? = null
    private val binding get() = _binding!!

    private var currentQuestionIndex = 0

    private val questionList: List<QuestionAnswer> = createQuestionList()

    private var selectedAnswerIndex: Int = -1 // Inisialisasi jawaban yang dipilih

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPilihanGandaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showQuestionAndAnswers(currentQuestionIndex)

        binding.btnSubmit.setOnClickListener {
            val currentQuestion = questionList[currentQuestionIndex]
            val correctAnswerIndex = currentQuestion.correctAnswerIndex


            if (selectedAnswerIndex == correctAnswerIndex) {

                navigateToNextQuestion()
            } else {
               Toast.makeText(context, "Jawaban salah", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnA.setOnClickListener {
            selectedAnswerIndex = 0
            updateButtonState()
        }

        binding.btnB.setOnClickListener {
            selectedAnswerIndex = 1
            updateButtonState()
        }

        binding.btnC.setOnClickListener {
            selectedAnswerIndex = 2
            updateButtonState()
        }

        binding.btnD.setOnClickListener {
            selectedAnswerIndex = 3
            updateButtonState()
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateButtonState() {
        // Reset semua tombol ke tampilan semula
        binding.btnA.isPressed = false
        binding.btnB.isPressed = false
        binding.btnC.isPressed = false
        binding.btnD.isPressed = false

        // Atur tombol yang dipilih dengan tampilan tertekan
        when (selectedAnswerIndex) {
            0 -> binding.btnA.isPressed = true
            1 -> binding.btnB.isPressed = true
            2 -> binding.btnC.isPressed = true
            3 -> binding.btnD.isPressed = true
        }
    }

    private fun showQuestionAndAnswers(questionAnswer: Int) {
        // Tampilkan pertanyaan
        val currentOption = questionList[questionAnswer]


        binding.btnA.text = currentOption.options[0]
        binding.btnB.text = currentOption.options[1]
        binding.btnC.text = currentOption.options[2]
        binding.btnD.text = currentOption.options[3]

    }

    private fun createQuestionList(): List<QuestionAnswer> {

        val question1 = QuestionAnswer(
            "Apa ibukota Indonesia?",
            listOf("Jakarta", "Bandung", "Surabaya", "Yogyakarta"),
            0 // Jakarta adalah jawaban yang benar
        )

        val question2 = QuestionAnswer(
            "Siapakah presiden Indonesia saat ini?",
            listOf("Joko Widodo", "Susilo Bambang Yudhoyono", "Megawati Soekarnoputri", "Abdurrahman Wahid"),
            0 // Joko Widodo adalah jawaban yang benar
        )
        // ...
        return listOf(question1, question2)
    }

    private fun navigateToNextQuestion() {
        if (currentQuestionIndex < questionList.size - 1) {
            currentQuestionIndex++
            selectedAnswerIndex = -1 // Reset jawaban yang dipilih
            showQuestionAndAnswers(currentQuestionIndex)
        } else {
            val currentQuestion = questionList[currentQuestionIndex]
            val correctAnswerIndex = currentQuestion.correctAnswerIndex

            if (selectedAnswerIndex == correctAnswerIndex) {

                val fragment = EssayFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {

                Toast.makeText(context, "Jawaban salah", Toast.LENGTH_SHORT).show()
            }
        }
    }


}