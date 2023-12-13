package com.example.signify_ch2_ps093.ui.materigrup

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ApiConfig
import com.example.signify_ch2_ps093.data.network.ApiService
import com.example.signify_ch2_ps093.data.network.DictionaryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailItemActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item)

        apiService = ApiConfig.getApiService()
        recyclerView = findViewById(R.id.rv_detail_item)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val selectedCategory = intent.getStringExtra("CATEGORY_NAME")

        val TvTitle : TextView = findViewById(R.id.tv_title_detail_item)
        TvTitle.text = selectedCategory

        fetchDataFromApi(selectedCategory)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })

    }

    private fun fetchDataFromApi(selectedCategory: String?) {
        apiService.getDictionary().enqueue(object : Callback<DictionaryResponse> {
            override fun onResponse(call: Call<DictionaryResponse>, response: Response<DictionaryResponse>) {
                if (response.isSuccessful) {
                    val dictionaryResponse = response.body()
                    dictionaryResponse?.let {
                        val materials = it.materials
                        // Filter item berdasarkan kategori yang dipilih
                        val filteredItems = materials?.find { material -> material?.category == selectedCategory }?.listItems.orEmpty()
                        adapter = DetailItemAdapter(filteredItems)
                        recyclerView.adapter = adapter

                    }
                } else {
                    Toast.makeText(this@DetailItemActivity, "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DictionaryResponse>, t: Throwable) {
                Toast.makeText(this@DetailItemActivity, "Network failure", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
