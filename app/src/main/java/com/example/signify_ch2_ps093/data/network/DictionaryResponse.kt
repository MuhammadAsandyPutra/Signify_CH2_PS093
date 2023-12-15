package com.example.signify_ch2_ps093.data.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DictionaryResponse(
	@SerializedName("materials")
	val materials: List<MaterialsItem>? = null
) : Parcelable

@Parcelize
data class MaterialsItem(
	@SerializedName("list_items")
	val listItems: List<ListItem>? = null,

	@SerializedName("total")
	val totals: String? = null,

	@SerializedName("img")
	val img: String? = null,

	@SerializedName("category")
	val category: String? = null
) : Parcelable

@Parcelize
data class ListItem(
	@SerializedName("name")
	val name: String? = null,

	@SerializedName("link")
	val link: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("desc")
	val desc: String? = null
) : Parcelable
