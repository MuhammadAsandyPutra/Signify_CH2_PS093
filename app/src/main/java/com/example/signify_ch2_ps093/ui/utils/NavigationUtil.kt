package com.example.signify_ch2_ps093.ui.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.ui.home.HomeActivity
import com.example.signify_ch2_ps093.ui.materigrup.MateriGrupActivity
import com.example.signify_ch2_ps093.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavigationUtil {

    fun showNavBar(bottomNavigationView: BottomNavigationView, activity: AppCompatActivity) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    val intent = Intent(activity, ProfileActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                    true
                }
                R.id.home -> {
                    val intent = Intent(activity, HomeActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                    true
                }
                R.id.dictionary -> {
                    val intent = Intent(activity, MateriGrupActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                    true
                }

                else -> false
            }
        }
    }

}