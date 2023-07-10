package com.example.faithconx.firebasedbpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityUploadObjectBinding
import com.example.faithconx.firebasedbpractice.model.Profile
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.Random

class UploadObjectActivity : AppCompatActivity() {
    companion object{
        private const val PATH = "OBJECTS"
    }
    private val firebaseDatabase= FirebaseDatabase.getInstance()
    private lateinit var binding: ActivityUploadObjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUploadObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnclickListener()
    }

    private fun setOnclickListener() {
        binding.btnSave.setOnClickListener { saveToFirebaseDb() }
    }

    private fun saveToFirebaseDb() {
        val name = binding.editTextName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        firebaseDatabase.reference.child("MyDB").child("Profiles").child("Users${Random().nextInt(100).toString()}").setValue(Profile(name,email,password))
    }
}