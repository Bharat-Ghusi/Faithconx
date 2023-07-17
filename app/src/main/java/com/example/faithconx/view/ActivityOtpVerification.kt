package com.example.faithconx.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityOtpVerificationBinding
import com.example.faithconx.main.ui.MainActivity
import com.example.faithconx.util.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ActivityOtpVerification : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private var verificationId = ""
    private lateinit var   authCredential:PhoneAuthCredential
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding?.let {
            it.btnGetOtp.setOnClickListener { onGetOtpClick(it) }
            it.btnSubmit.setOnClickListener { onSubmitClick(it) }
        }
    }

    //    Send a verification code to the user's phone
    fun onGetOtpClick(view: View) {
        //Register cpp with number
        binding.ccp.registerCarrierNumberEditText(binding.editTextPhoneNumber)
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(binding.ccp.fullNumberWithPlus)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    verificationId = otp //OTP ID
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@ActivityOtpVerification, e.message, Toast.LENGTH_LONG)
                        .show()
                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        setVisibility(binding.groupSubmitOtp.id, View.VISIBLE)
    }
    fun onSubmitClick(view: View) {
        binding.editTextOtpSubmit.text.toString()?.let {
            if(it.isNotEmpty() && it.length == Constants.OTP_LENGTH){
                //Progress Bar Enabled.
                binding?.otpProgressBar?.visibility = View.VISIBLE
                authCredential = PhoneAuthProvider.getCredential(verificationId,it)
                signInWithPhoneAuthCredential(authCredential)
            }
        }

    }
    private fun setVisibility(id: Int, visibility: Int) {
        when(id){
            R.id.groupGetOtp -> binding.groupGetOtp.visibility= visibility
            R.id.groupSubmitOtp -> binding.groupSubmitOtp.visibility= visibility
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Progress Bar Disabled.
                    binding?.otpProgressBar?.visibility = View.GONE
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //Progress Bar Disabled.
                    binding?.otpProgressBar?.visibility = View.GONE
                    binding?.editTextOtpSubmitInputLayout?.isErrorEnabled = true
                    binding?.editTextOtpSubmitInputLayout?.error = getString(R.string.wrongOtpEntered)

                }
            }
    }



}