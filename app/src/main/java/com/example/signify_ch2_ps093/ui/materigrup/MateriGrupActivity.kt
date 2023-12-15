package com.example.signify_ch2_ps093.ui.materigrup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ApiConfig
import com.example.signify_ch2_ps093.data.network.DictionaryResponse
import com.example.signify_ch2_ps093.data.network.ListItem
import com.example.signify_ch2_ps093.data.network.MaterialsItem
import com.example.signify_ch2_ps093.databinding.ActivityMateriGrupBinding
import com.example.signify_ch2_ps093.ui.utils.NavigationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MateriGrupActivity : AppCompatActivity(), CategoryAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMateriGrupBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriGrupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavBar
        NavigationUtil.showNavBar(bottomNavigationView, this)

        categoryAdapter = CategoryAdapter(this)
        categoryAdapter.setOnItemClickListener(this)

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
                        val categoryItemsMap = createCategoryItemsMap(it)
                        categoryAdapter.setCategoryItemsMap(categoryItemsMap)
                        categoryAdapter.setData(it)
                    }
                }
            }

            override fun onFailure(call: Call<DictionaryResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun createCategoryItemsMap(materials: List<MaterialsItem?>): Map<String, List<ListItem>> {
        val categoryItemsMap = mutableMapOf<String, List<ListItem>>()
        materials.forEach { material ->
            material?.category?.let { category ->
                categoryItemsMap[category] = material.listItems ?: emptyList()
            }
        }
        return categoryItemsMap
    }


    override fun onItemClick(materialsItem: MaterialsItem) {
        val categoryName = materialsItem.category ?: ""
        val intent = Intent(this, DetailItemActivity::class.java)
        intent.putExtra("CATEGORY_NAME", categoryName)

        val listItems = categoryAdapter.getCategoryItemsMap()[categoryName]
        val listItemNames = listItems?.joinToString("\n") { it.name ?: "" }
        intent.putExtra("LIST_ITEM_NAMES", listItemNames)

        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavBar.menu.findItem(R.id.dictionary)?.isChecked = true
    }
}
