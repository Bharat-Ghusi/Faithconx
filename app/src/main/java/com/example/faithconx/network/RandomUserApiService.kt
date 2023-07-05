package com.example.faithconx.network

import com.example.faithconx.model.RandomUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApiService {
    @GET("/api/")
    fun getUsers(@Query("results") results:Int):Call<RandomUsers>
}