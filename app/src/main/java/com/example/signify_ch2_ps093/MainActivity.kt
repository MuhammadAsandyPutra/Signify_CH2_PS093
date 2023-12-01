package com.example.signify_ch2_ps093

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}