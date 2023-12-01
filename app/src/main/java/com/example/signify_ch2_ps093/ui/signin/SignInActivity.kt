package com.example.signify_ch2_ps093.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivitySignInBinding
import com.example.signify_ch2_ps093.ui.login.LoginActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDirectToLogin.setOnClickListener{
            val intent = Intent(this@SignInActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
//            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}