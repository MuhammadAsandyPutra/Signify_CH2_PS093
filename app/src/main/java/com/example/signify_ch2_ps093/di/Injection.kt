package com.example.signify_ch2_ps093.di

import android.content.Context
import com.example.signify_ch2_ps093.data.Repository
import com.example.signify_ch2_ps093.data.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService, context)
    }
}
