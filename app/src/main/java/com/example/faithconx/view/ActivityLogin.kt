package com.example.faithconx.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding?.let {
            it.btnLoginWithPhoneNumber.setOnClickListener { onClickLoginWithPhoneNumber(it) }
        }
    }

    fun onClickLoginWithPhoneNumber(view: View) {
        startActivity(Intent(this,ActivityOtpVerification::class.java))
    }
}