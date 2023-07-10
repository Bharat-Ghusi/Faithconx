package com.example.faithconx.firebasedbpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivitySigninBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnCLickListener()
    }

    private fun setOnCLickListener() {
        binding.btnLogin.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(this@SigninActivity,
        object : OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if(task.isSuccessful){
                    startActivity(Intent(this@SigninActivity,HomeActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@SigninActivity,"Wrong password or email",Toast.LENGTH_LONG).show()
                }
            }

        })
    }
}

