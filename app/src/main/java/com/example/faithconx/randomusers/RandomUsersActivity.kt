package com.example.faithconx.randomusers

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.network.RandomUsersApiClients
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_users)
        getRandomUsers()
    }

    private fun getRandomUsers() {
        val randomUsersApiClient = RandomUsersApiClients().randomUserApiService
        val call = randomUsersApiClient.getUsers(5)
        call.enqueue(object : Callback<RandomUsers?> {
            override fun onResponse(call: Call<RandomUsers?>, response: Response<RandomUsers?>) {
                if (response.isSuccessful) {

                    Log.i("TAG", response.body().toString())
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





