package com.example.signify_ch2_ps093.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.ArticleModel
import com.example.signify_ch2_ps093.data.HomeModel
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityHomeBinding
import com.example.signify_ch2_ps093.ui.utils.Constant
import com.example.signify_ch2_ps093.ui.utils.Constant.SESSION
import com.example.signify_ch2_ps093.ui.utils.NavigationUtil

class HomeActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var rvHome: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var adapterArticle: HomeArticleAdapter
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvHome2: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavBar
        NavigationUtil.showNavBar(bottomNavigationView, this)


        setupView()

    }

    private fun setupView() {
        val sharedPref = UserPreference.init(this, SESSION)
        val token = sharedPref.getString(Constant.TOKEN, "")
        val usernames = UserPreference.getUsername(applicationContext)

        if (token != "") {
            binding.usernameHeader.text = buildString {
                append("Hello, ")
                append(usernames)
            }
            rvHome = binding.rv1
            rvHome2 = binding.rv2
            adapter = HomeAdapter(getListData())
            adapterArticle = HomeArticleAdapter(getArticleListData(), this)

            rvHome.setHasFixedSize(true)
            rvHome2.setHasFixedSize(true)
            rvHome.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvHome2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvHome.adapter = adapter
            rvHome2.adapter = adapterArticle
        }
    }

    private fun getListData(): ArrayList<HomeModel> {
        val titles = resources.getStringArray(R.array.data_item_home_title)
        val messages = resources.getStringArray(R.array.data_item_home_message)
        val photos = resources.obtainTypedArray(R.array.data_item_home_image)

        val data = ArrayList<HomeModel>()

        for (i in titles.indices) {
            data.add(HomeModel(titles[i], messages[i], photos.getResourceId(i, 0)))
        }

        photos.recycle()

        return data
    }

    private fun getArticleListData(): ArrayList<ArticleModel> {
        val titles = resources.getStringArray(R.array.data_article_title)
        val messages = resources.getStringArray(R.array.data_article_message)
        val photos = resources.obtainTypedArray(R.array.data_article_image)
        val links = resources.getStringArray(R.array.data_article_link)

        val data = ArrayList<ArticleModel>()

        for (i in titles.indices) {
            data.add(ArticleModel(titles[i], messages[i], photos.getResourceId(i, 0), links[i]))
        }

        photos.recycle()

        return data
    }


    override fun onResume() {
        super.onResume()
        binding.bottomNavBar.menu.findItem(R.id.home)?.isChecked = true
        val updatedUsername = UserPreference.getUsername(applicationContext)
        binding.usernameHeader.text = buildString {
            append("Hello, ")
            append(updatedUsername)
        }
    }

    override fun onItemClick(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}