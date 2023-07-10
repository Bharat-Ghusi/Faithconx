package com.example.faithconx.login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityLoginBinding
import com.example.faithconx.view.ActivityOtpVerification
import com.example.faithconx.signup.ui.ActivitySignup
import com.example.faithconx.main.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
        onFocusChange()
    }

    private fun onFocusChange() {
        binding.email.setOnFocusChangeListener { v, hasFocus ->  emailBoxFocusChange(v,hasFocus) }
        binding.password.setOnFocusChangeListener { v, hasFocus ->  passwordBoxFocusChange(v,hasFocus) }
    }

    private fun passwordBoxFocusChange(v: View?, hasFocus: Boolean) {
        //Change to when and optimize
        if(!hasFocus){
            if(binding.password.text.toString().length < 4){
            binding.passwordInputLayout.isHelperTextEnabled = true
                binding.passwordInputLayout.helperText = "Field cannot be less than 5."

            }
            else
                binding.btnLogin.setBackgroundTintList(this.getResources().getColorStateList(R.color.loginbtn_color));
        }else{
            binding.passwordInputLayout.isHelperTextEnabled = false

        }
    }

    private fun emailBoxFocusChange(v: View?, hasFocus: Boolean) {
        //Change to when and optimize
       if(!hasFocus){
           if(binding.email.text.toString().length < 4){
           binding.emailInputLayout.isHelperTextEnabled = true
           binding.emailInputLayout.helperText = "Field cannot be less than 5."

           }
       }else{
           binding.emailInputLayout.isHelperTextEnabled = false
       }
    }

    private fun setOnClickListener() {
  //Change to VIew.onClickListener()
        //use when and scope function
            binding.btnLoginWithPhoneNumber.setOnClickListener { onClickLoginWithPhoneNumber(it) }
            binding.btnLogin.setOnClickListener { onLoginClick(it) }
            binding.createOne.setOnClickListener { onCreateOneClick(it) }

    }

    fun onClickLoginWithPhoneNumber(view: View) {
        startActivity(Intent(this, ActivityOtpVerification::class.java))
    }
//sepa
    fun onLoginClick(view: View) {
        if(validateUserCred()) {
            //use string resource
            Toast.makeText(this, "Password or email must be 8 digit", Toast.LENGTH_SHORT).show()
            return
        }
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(this@ActivityLogin
        ) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                finish()
            } else {
                binding.passwordInputLayout.isHelperTextEnabled = true
                binding.passwordInputLayout.helperText = "Email or password is wrong."
            }
        }
    }

    private fun validateUserCred(): Boolean {
        return binding.email.text.toString().length < 8 && binding.password.text.toString().length  < 5
    }



    fun onCreateOneClick(view: View) {
        startActivity(Intent(this, ActivitySignup::class.java))
    }
}