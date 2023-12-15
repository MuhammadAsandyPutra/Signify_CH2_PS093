package com.example.signify_ch2_ps093.data.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResponse(

	@field:SerializedName("quizess")
	val quizess: List<QuizessItem>? = null
): Parcelable

@Parcelize
data class QuizessItem(

	@field:SerializedName("Level_name")
	val levelName: String? = null,

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("total")
	val totals: String? = null,

	@field:SerializedName("img_open")
	val imgOpen: String? = null,

	@field:SerializedName("img_locked")
	val imgLocked: String? = null,

	@field:SerializedName("content")
	val content: List<ContentItem>? = null,

) : Parcelable

@Parcelize
data class ContentItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("options")
	val options: List<String>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("levelContent")
	val levelContent: Int? = null
) : Parcelable
