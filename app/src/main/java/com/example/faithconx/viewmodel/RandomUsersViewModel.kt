package com.example.faithconx.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.network.RandomUsersApiClients
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomUsersViewModel: ViewModel() {
private val randomUsersMutableData = MutableLiveData<RandomUsers>()
    fun getRandomUsers():LiveData<RandomUsers> = randomUsersMutableData

     fun fetchRandomUsersFromApi() {
        val randomUsersApiClient = RandomUsersApiClients().randomUserApiService
        val call = randomUsersApiClient.getUsers(5)
        call.enqueue(object : Callback<RandomUsers?> {
            override fun onResponse(call: Call<RandomUsers?>, response: Response<RandomUsers?>) {
                if (response.isSuccessful) {
                    val randomUsers = response.body()
                    randomUsersMutableData.value = randomUsers
                    Log.i("TAG", randomUsers.toString())
                } else {
                    Log.i("TAG", "CODE: ${response.code().toString()}")
                }
            }

            override fun onFailure(call: Call<RandomUsers?>, t: Throwable) {
                Log.e("TAG", "FAILURE: ${t.message}")
            }
        })
    }
}