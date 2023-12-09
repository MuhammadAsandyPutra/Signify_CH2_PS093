package com.example.signify_ch2_ps093.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.Result
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivitySignUpBinding
import com.example.signify_ch2_ps093.ui.login.LoginActivity
import com.example.signify_ch2_ps093.ui.utils.ViewModelFactory
import com.example.signify_ch2_ps093.ui.utils.hide
import com.example.signify_ch2_ps093.ui.utils.show
import com.example.signify_ch2_ps093.ui.utils.toast

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))[SignupViewModel::class.java]

        binding.tvDirectToLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
//            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        }
        setupAction()
    }
    private fun signup(name: String, email: String, password: String) {
        viewModel.signup(name, email, password).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.btnSignUp.text = ""
                    binding.progressBar.show()
                }

                is Result.Success -> {
                    binding.progressBar.hide()
                    AlertDialog.Builder(this).apply {
                        setTitle("Register")
                        setMessage(getString(R.string.signup_succeed))
                        setPositiveButton(getString(R.string.continue_login)) { _, _ ->
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    binding.progressBar.hide()
                    binding.btnSignUp.text = getString(R.string.daftar)
                    toast(it.error)
                }

                else -> {}
            }
        }
    }

    private fun setupAction() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()


            signup(name, email, password)
            UserPreference.saveUserInfo(username = name, email, applicationContext)
        }
    }
}