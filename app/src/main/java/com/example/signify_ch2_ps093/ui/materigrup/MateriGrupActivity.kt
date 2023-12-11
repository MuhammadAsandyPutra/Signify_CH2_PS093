package com.example.signify_ch2_ps093.ui.materigrup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.signify_ch2_ps093.data.network.ApiConfig
import com.example.signify_ch2_ps093.data.network.DictionaryResponse
import com.example.signify_ch2_ps093.data.network.MaterialsItem
import com.example.signify_ch2_ps093.databinding.ActivityMateriGrupBinding
import com.example.signify_ch2_ps093.ui.utils.NavigationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MateriGrupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMateriGrupBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriGrupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavBar
        NavigationUtil.showNavBar(bottomNavigationView, this)

        categoryAdapter = CategoryAdapter()

        binding.rvMateriGrup.apply {
            layoutManager = LinearLayoutManager(this@MateriGrupActivity)
            adapter = categoryAdapter
        }

        fetchData()
    }

    private fun fetchData() {
        val apiService = ApiConfig.getApiService()
        apiService.getDictionary().enqueue(object : Callback<DictionaryResponse> {
            override fun onResponse(
                call: Call<DictionaryResponse>,
                response: Response<DictionaryResponse>
            ) {
                if (response.isSuccessful) {
                    val dictionaryResponse: DictionaryResponse? = response.body()
                    val materials: List<MaterialsItem?>? = dictionaryResponse?.materials
                    materials?.let {
                        categoryAdapter.setData(it)
                    }
                }
            }

            override fun onFailure(call: Call<DictionaryResponse>, t: Throwable) {
               t.printStackTrace()
            }
        })
    }
}
