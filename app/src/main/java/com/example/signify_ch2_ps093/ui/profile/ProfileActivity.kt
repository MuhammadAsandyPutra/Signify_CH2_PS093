package com.example.signify_ch2_ps093.ui.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.ProfileModel
import com.example.signify_ch2_ps093.data.network.ApiConfig
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityProfileBinding
import com.example.signify_ch2_ps093.ui.login.LoginActivity
import com.example.signify_ch2_ps093.ui.utils.NavigationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding


    private val selectedGenderLiveData = MutableLiveData<Int>()
    private val selectedDateLiveData = MutableLiveData<String>()

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


        binding.IvBod.setOnClickListener {
            showDatePicker()
        }

        binding.TvLogout.setOnClickListener{
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            UserPreference.logOut(applicationContext)
        }

        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this@ProfileActivity, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        selectedGenderLiveData.observe(this, Observer { gender ->
            UserPreference.saveSelectedDate(applicationContext, selectedDateLiveData.value ?: "", gender, "")
        })


        fetchData()
    }


    private fun fetchData(){
        val apiService = ApiConfig.getApiService()
        apiService.getProfile().enqueue(object: Callback<ProfileModel>{
            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                if (response.isSuccessful){
                    val userProfile = response.body()

                    userProfile?.let {
                        binding.TvBod.text = it.bday.toString()
                        binding.TvPhone.setText(it.phone)

                        val genderIndex = if (it.gender == 0) 0 else 1
                        binding.spinnerGender.setSelection(genderIndex)
                    }

                }
            }

            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavBar.menu.findItem(R.id.profile)?.isChecked = true
    }

    private fun showDatePicker(){
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, {_, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
                binding.TvBod.text = formattedDate

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


}
