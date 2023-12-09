package com.example.signify_ch2_ps093.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityProfileBinding
import com.example.signify_ch2_ps093.ui.utils.NavigationUtil

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavBar

        NavigationUtil.showNavBar(bottomNavigationView, this)

        val usernames = UserPreference.getUsername(applicationContext)
        val emails = UserPreference.getEmail(applicationContext)

        binding.TvUsername.text = usernames
        binding.TvEmail.text = emails
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavBar.menu.findItem(R.id.profile)?.isChecked = true
    }


}
