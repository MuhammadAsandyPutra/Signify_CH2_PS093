package com.example.signify_ch2_ps093.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivitySplashScreenIconBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenIcon : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenIconBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenIconBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}