package com.example.signify_ch2_ps093.data.network

import com.google.gson.annotations.SerializedName

data class Responses(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,
)

data class LoginResult(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field: SerializedName("token")
    val token: String? = null
)