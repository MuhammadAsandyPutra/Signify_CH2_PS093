package com.example.signify_ch2_ps093.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}