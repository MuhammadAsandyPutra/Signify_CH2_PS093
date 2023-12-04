package com.example.signify_ch2_ps093.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.databinding.ActivityLoginBinding
import com.example.signify_ch2_ps093.ui.home.HomeActivity
import com.example.signify_ch2_ps093.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDirectToSignIn.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
//            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}