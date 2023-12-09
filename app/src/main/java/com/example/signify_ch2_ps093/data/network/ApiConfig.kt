package com.example.signify_ch2_ps093.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val interceptor = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api-test-405015.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(interceptor)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
//https://api-test-405015.et.r.appspot.com/
//https://story-api.dicoding.dev/v1/
