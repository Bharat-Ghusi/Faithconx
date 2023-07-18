package com.example.faithconx.main.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.model.User
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProfileViewModel: ViewModel() {
    private val uid = FirebaseAuth.getInstance().uid
    private val userObj = MutableLiveData<User?>()
    private val taskState = MutableLiveData<Boolean>()

    fun getTaskState():LiveData<Boolean> = taskState
    fun getUser():LiveData<User?> = userObj
    fun fetchCurrentUserData(){
        uid?.let { uid ->
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB_NAME).child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskState.postValue(true)
                val user = snapshot.getValue<User>()
                userObj.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                taskState.postValue(false)
                Log.i("TAG","Failed to fetch user profile data.${error.message}")
            }
        })

        }
    }
}