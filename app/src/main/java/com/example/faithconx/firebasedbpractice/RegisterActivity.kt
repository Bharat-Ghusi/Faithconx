package com.example.faithconx.firebasedbpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnSubmit.setOnClickListener { registerUser() }
        binding.btnLogin.setOnClickListener { redirectToSigninPage() }
    }

    private fun redirectToSigninPage() {
        startActivity(Intent(this,SigninActivity::class.java))
    }

    private fun registerUser() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Field cannot be empty.",Toast.LENGTH_LONG).show()
        }
        else if(email.length < 4 || password.length < 4){
            Toast.makeText(this,"Enter at least five digit.",Toast.LENGTH_LONG).show()
        }
        else{
            registerUserUtil(email,password)

        }
    }

    private fun registerUserUtil(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this@RegisterActivity,object : OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if(task.isSuccessful){
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@RegisterActivity,"Enter at least six digit.",Toast.LENGTH_LONG).show()
                }
            }

        })

    }
}