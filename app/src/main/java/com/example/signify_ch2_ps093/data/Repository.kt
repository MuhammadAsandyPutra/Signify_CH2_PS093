package com.example.signify_ch2_ps093.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.signify_ch2_ps093.data.network.ApiService
import com.example.signify_ch2_ps093.data.network.LoginRequest
import com.example.signify_ch2_ps093.data.network.RegisterRequest
import com.example.signify_ch2_ps093.data.network.Responses
import com.example.signify_ch2_ps093.data.pref.UserModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService
) {
    private val result = MediatorLiveData<Result<UserModel>>()
    private val uploadResult = MediatorLiveData<Result<String>>()

    fun signup(name: String, email: String, password: String): LiveData<Result<UserModel>> {
        result.value = Result.Loading

        val client = apiService.register(request = RegisterRequest(name, email, password))
        client.enqueue(object : Callback<Responses> {
            override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                try {
                    if (response.isSuccessful) {
                        val user = UserModel(
                            name,
                            email,
                            "null",
                            false
                        )

                        result.value = Result.Success(user)
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, Responses::class.java)
                    result.value = Result.Error(errorBody.message)
                }
            }

            override fun onFailure(call: Call<Responses>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })

        return result
    }

    fun login(email: String, password: String): LiveData<Result<String>> {
        uploadResult.value = Result.Loading

        val client = apiService.login(request = LoginRequest(email, password))
        client.enqueue(object : Callback<Responses> {
            override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            uploadResult.value = Result.Success(responseBody.loginResult!!.token!!)

                        }
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, Responses::class.java)
                    uploadResult.value = Result.Error(errorBody.message)
                }
            }

            override fun onFailure(call: Call<Responses>, t: Throwable) {
                uploadResult.value = Result.Error(t.message.toString())
            }

        })

        return uploadResult
    }

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            //database: Database
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}