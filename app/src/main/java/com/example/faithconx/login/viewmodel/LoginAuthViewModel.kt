package com.example.faithconx.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginAuthViewModel:ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authenticationState=MutableLiveData<Boolean>(false)
    fun getAuthenticationState():LiveData<Boolean> = authenticationState

    fun loginUser(email:String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){ task ->
            if(task.isSuccessful){
                authenticationState.postValue(true)
            }else{
                authenticationState.postValue(false)
            }
        }
    }
}