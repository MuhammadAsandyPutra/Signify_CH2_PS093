package com.example.signify_ch2_ps093.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.signify_ch2_ps093.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String, context: Context) = repository.login(email, password)
}