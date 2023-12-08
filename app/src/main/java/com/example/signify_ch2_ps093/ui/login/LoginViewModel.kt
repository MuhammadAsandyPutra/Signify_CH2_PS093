package com.example.signify_ch2_ps093.ui.login

import androidx.lifecycle.ViewModel
import com.example.signify_ch2_ps093.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
}