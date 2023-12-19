package com.example.signify_ch2_ps093.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class untuk menyimpan jawaban dari fragment PilihanGandaFragment

@Parcelize
data class MultipleChoiceAnswer(
    val questionIndex: Int,
    val selectedAnswerIndex: Int
): Parcelable

// Data class untuk menyimpan jawaban dari fragment EssayFragment

@Parcelize
data class EssayAnswer(
    val questionIndex: Int,
    val userAnswer: String
) : Parcelable

@Parcelize
data class PeragakanAnswer(
    val resultLabel: String
) : Parcelable


@Parcelize
// Data class untuk menyimpan gabungan jawaban dari kedua fragment
data class ResultModels(
    val multipleChoiceAnswers: List<MultipleChoiceAnswer>,
    val essayAnswers: List<EssayAnswer>,
    val peragakanAnswer: List<PeragakanAnswer>
): Parcelable
