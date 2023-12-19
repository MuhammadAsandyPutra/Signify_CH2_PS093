package com.example.signify_ch2_ps093.data.pref


import android.content.Context
import android.content.SharedPreferences
import com.example.signify_ch2_ps093.ui.utils.Constant.SESSION
import com.example.signify_ch2_ps093.ui.utils.Constant.TOKEN


object UserPreference {

    fun init(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun preferenceEditor(context: Context): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveToken(token: String, context: Context) {
        val editor = preferenceEditor(context)
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun logOut(context: Context) {
        val editor = preferenceEditor(context)
        editor.remove(TOKEN)
        editor.remove("name")
        editor.remove("email")
        editor.remove("status")
        editor.apply()
    }

    fun saveUserInfo(name: String, email: String, context: Context) {
        val editor = preferenceEditor(context)
        editor.putString("name", name)
        editor.putString("email", email)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.getString("name", null)
    }

    fun getEmail(context: Context): String? {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.getString("email", null)
    }


    fun getUserLevel(context: Context): Int {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.getInt("user_level", 1)
    }

}