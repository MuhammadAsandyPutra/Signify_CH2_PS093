package com.example.signify_ch2_ps093.ui.signup

import androidx.lifecycle.ViewModel
import com.example.signify_ch2_ps093.data.Repository

class SignupViewModel(
    private val repository: Repository
): ViewModel() {
    fun signup(name: String, email: String, password: String) = repository.signup(name, email, password)
}