package com.example.signify_ch2_ps093.data.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

//    @FormUrlEncoded
//    @POST("register")
//
//    fun register(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<Responses>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<Responses>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<Responses>

//    @FormUrlEncoded
//    @POST("login")
//    fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<Responses>
}