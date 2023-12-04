package com.example.signify_ch2_ps093.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.signify_ch2_ps093.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var rvHome : RecyclerView
    private lateinit var adapter : HomeAdapter
    private lateinit var binding : ActivityHomeBinding
    private lateinit var rvHome2 : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvHome = binding.rv1
        rvHome2 = binding.rv2
        adapter = HomeAdapter()

        rvHome.setHasFixedSize(true)
        rvHome2.setHasFixedSize(true)
        rvHome.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvHome2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvHome.adapter = adapter
        rvHome2.adapter = adapter


    }

}