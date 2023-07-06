package com.example.faithconx.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result
import com.example.faithconx.network.RandomUsersApiClients
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomUsersViewModel: ViewModel() {
private val randomUsersMutableData = MutableLiveData<MutableList<Result>?>()
   private val isVisible = MutableLiveData<Boolean>(false)
    fun getRandomUsers():LiveData<MutableList<Result>?> = randomUsersMutableData
    fun getProgressVisibility():LiveData<Boolean> =isVisible

    init {
        //Data fetch from API through retrofit
        fetchRandomUsersFromApi()
    }


    private  fun fetchRandomUsersFromApi() {
         isVisible.postValue(true) //To set progress bar
        val randomUsersApiClient = RandomUsersApiClients().randomUserApiService
        val call = randomUsersApiClient.getUsers(5)
        call.enqueue(object : Callback<RandomUsers?> {
            override fun onResponse(call: Call<RandomUsers?>, response: Response<RandomUsers?>) {
                if (response.isSuccessful) {
                    isVisible.postValue(false)
                    val randomUsers = response.body()
                    val results = randomUsers?.results
                    randomUsersMutableData.postValue(results?.toMutableList())
                    Log.i("TAG", randomUsers.toString())
                } else {
                    Log.i("TAG", "CODE: ${response.code().toString()}")
                    isVisible.value= false
                }
            }

            override fun onFailure(call: Call<RandomUsers?>, t: Throwable) {
                isVisible.postValue(false)
                Log.e("TAG", "FAILURE: ${t.message}")
            }
        })
    }
}