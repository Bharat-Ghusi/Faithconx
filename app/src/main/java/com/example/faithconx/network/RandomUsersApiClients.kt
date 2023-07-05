package com.example.faithconx.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RandomUsersApiClients {


    companion object {
        private val BASE_URL = "https://randomuser.me/"
    }

private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
    val randomUserApiService by lazy {
        retrofit.create(RandomUserApiService::class.java)
    }

}