package com.example.signify_ch2_ps093.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.Result
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityLoginBinding
import com.example.signify_ch2_ps093.ui.home.HomeActivity
import com.example.signify_ch2_ps093.ui.signup.SignUpActivity
import com.example.signify_ch2_ps093.ui.utils.Constant
import com.example.signify_ch2_ps093.ui.utils.ViewModelFactory
import com.example.signify_ch2_ps093.ui.utils.hide
import com.example.signify_ch2_ps093.ui.utils.show
import com.example.signify_ch2_ps093.ui.utils.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginViewModel::class.java]

        binding.tvDirectToSignIn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
//            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        observer()
        setupLogin()

    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                    binding.btnLogin.text = ""
                }

                is Result.Success -> {
                    loginProcess(result.data)
                    binding.progressBar.hide()

                }

                is Result.Error -> {
                    binding.progressBar.hide()
                    binding.btnLogin.text = getString(R.string.login)
                    toast(result.error)
                }

                else -> {}
            }
        }
    }

    private fun observer() {
        val sharedPref = UserPreference.init(this, "session")
        val token = sharedPref.getString(Constant.TOKEN, "")


        if (token != "") {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loginProcess(token: String) {
        Log.d("LoginProcess", "Received token: $token")
        UserPreference.saveToken(token, this)
        UserPreference.getUsername(this)
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    private fun setupLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            login(email, password)
        }
    }
}