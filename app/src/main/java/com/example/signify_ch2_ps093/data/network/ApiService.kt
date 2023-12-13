package com.example.signify_ch2_ps093.data.network

import com.example.signify_ch2_ps093.data.ProfileModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    @POST("register")
    fun register(@Body request: RegisterRequest): Call<Responses>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<Responses>

    //=================================================================

    @GET("get/profile")
    fun getProfile(): Call<ProfileModel>

    @GET("/get/dict")
    fun getDictionary(): Call<DictionaryResponse>

    @GET("/get/quiz")
    fun getQuiz(): Call<QuizResponse>



}