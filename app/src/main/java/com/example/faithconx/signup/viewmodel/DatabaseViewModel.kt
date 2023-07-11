package com.example.faithconx.signup.viewmodel

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.model.User
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DatabaseViewModel: ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val dataSavingState = MutableLiveData<Boolean>()
    fun getDataSavingState():LiveData<Boolean> = dataSavingState


    fun saveUserDetailsToDb(firstName:String, lastName:String, email:String, number:String, firebaseAuth: FirebaseAuth){

        firebaseAuth?.uid?.let {
            firebaseDatabase.reference.child(Constants.USERS_DB_NAME)
                .child(it).setValue(User(firstName, lastName, email, number))
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        //Save to DB successful
                        dataSavingState.postValue(true)

                    } else {
                        //Save to DB Failed
                        dataSavingState.postValue(false)
                    }
                }
        }
    }
}