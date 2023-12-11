package com.example.signify_ch2_ps093.data.network

import com.google.gson.annotations.SerializedName

data class DictionaryResponse(

	@field:SerializedName("materials")
	val materials: List<MaterialsItem?>? = null
)

data class MaterialsItem(

	@field:SerializedName("list_item")
	val listItem: List<ListItemItem?>? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class ListItemItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)
