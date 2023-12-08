package com.example.signify_ch2_ps093.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)