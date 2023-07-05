package com.example.faithconx.network

import com.example.faithconx.model.RandomUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApiService {

    /**
     * Read before define Call<?> type hear
     * //https://stackoverflow.com/questions/36177629/retrofit2-android-expected-begin-array-but-was-begin-object-at-line-1-column-2
     */
    @GET("/api/")
    fun getUsers(@Query("results") results:Int):Call<RandomUsers>
}

