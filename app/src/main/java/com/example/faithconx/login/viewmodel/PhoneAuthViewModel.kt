package com.example.faithconx.login.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faithconx.R
import com.example.faithconx.main.ui.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import kotlin.math.log

class PhoneAuthViewModel(private val activity: Activity) {
    private val firebaseAuth =FirebaseAuth.getInstance()

    private val otpVerificationId = MutableLiveData<String>()
    private val loggedInState = MutableLiveData<Boolean>()

    fun getOtpVerificationId():LiveData<String> = otpVerificationId
    fun getLoggedInState():LiveData<Boolean> = loggedInState

    fun getOtp(number:String){
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    otpVerificationId.postValue(otp) //OTP ID
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("TAG","OTP verification failed. ${e.message}")
                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    //Progress Bar Disabled.
                    loggedInState.postValue(true)
                } else {
                    //Progress Bar Disabled.
                    loggedInState.postValue(false)

                }
            }
    }
}