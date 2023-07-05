package com.example.faithconx.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RandomUsersApiClients {

//https://stackoverflow.com/questions/36177629/retrofit2-android-expected-begin-array-but-was-begin-object-at-line-1-column-2
    companion object {
        private val BASE_URL = "https://randomuser.me/"
    }

    /**
     * . The property is initialized only when it is accessed for the first time,
     * and subsequent accesses to the property return the same value without reinitializing it.
     */
    private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
    val randomUserApiService: RandomUserApiService by lazy {
        retrofit.create(RandomUserApiService::class.java)
    }

}