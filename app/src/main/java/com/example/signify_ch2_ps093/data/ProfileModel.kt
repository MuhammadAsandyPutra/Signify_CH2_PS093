package com.example.signify_ch2_ps093.data

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("userId") val userId: String,
    val gender: Int,
    val bday: Int,
    val phone: String
)
