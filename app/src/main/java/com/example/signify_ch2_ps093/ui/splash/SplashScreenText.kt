package com.example.signify_ch2_ps093.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivitySplashScreenTextBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenText : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenTextBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}