package com.example.faithconx.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.signup.ui.ActivitySignup
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    companion object{
        private const val TAG = "AuthViewModel"
    }
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authenticationState = MutableLiveData<Boolean>()
    private val isLoggedIn = MutableLiveData<FirebaseUser>(firebaseAuth.currentUser)
    fun getAuthenticationState(): LiveData<Boolean> = authenticationState



    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    //Login success
                    authenticationState.postValue(true)
                } else {
                    // //Login Failed.
                    Log.i(TAG, Constants.AUHTENTICATION_FAILED_MSG,task?.exception)
                    authenticationState.postValue(false)
                }
            }
    }

}