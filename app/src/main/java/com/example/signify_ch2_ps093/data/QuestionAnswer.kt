package com.example.signify_ch2_ps093.data

data class QuestionAnswer(
    val question: String,
    val options: List<String>, // Daftar pilihan jawaban
    val correctAnswerIndex: Int
)
