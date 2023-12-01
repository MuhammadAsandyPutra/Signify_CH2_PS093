package com.example.signify_ch2_ps093.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivitySplashScreenIconBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenIcon : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenIconBinding
    private val SPLASH_DISPLAY_LENGTH = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenIconBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenIcon, SplashScreenText::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }
}