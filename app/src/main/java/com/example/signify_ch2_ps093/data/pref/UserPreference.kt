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
        editor.remove("status")
        editor.apply()
    }

    fun saveUserInfo(username: String, email: String, context: Context){
        val editor = preferenceEditor(context)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }

    fun getEmail(context: Context): String?{
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.getString("email", null)
    }

    fun saveSelectedDate(context: Context, selectedDate: String, selectedGender: Int, enteredPhone: String) {
        val editor = preferenceEditor(context)
        editor.putString("selected_date", selectedDate)
        editor.putInt("selected_gender", selectedGender)
        editor.putString("entered_phone", enteredPhone)
        editor.apply()
    }

    //pake sharedpreference dulu buat ngetest ui level
    fun getUserLevel(context: Context): Int {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.getInt("user_level", 1) // Default level 1 jika tidak ada nilai yang tersimpan
    }

}