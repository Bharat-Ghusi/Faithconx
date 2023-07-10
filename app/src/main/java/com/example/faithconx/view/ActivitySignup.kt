package com.example.faithconx.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.databinding.ActivitySignupBinding
import com.example.faithconx.model.User
import com.example.faithconx.util.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime

class ActivitySignup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onFocusChangeValidate()
        registerUser()
    }

    /**
     * This method will check if fields are at least contain something
     */
    private fun onFocusChangeValidate() {
        binding.firstName.setOnFocusChangeListener { v, hasFocus -> firstNameFocusCheck(hasFocus) }
        binding.lastName.setOnFocusChangeListener { v, hasFocus -> lastNameFocusCheck(hasFocus) }
        binding.email.setOnFocusChangeListener { v, hasFocus -> emailNameFocusCheck(hasFocus) }
        binding.editTextPhoneNumber.setOnFocusChangeListener { v, hasFocus ->
            phoneNumberFocusCheck(
                hasFocus
            )
        }
        binding.editTextPassword.setOnFocusChangeListener { v, hasFocus ->
            passwordFocusCheck(
                hasFocus
            )
        }
        binding.editTextConfirmPassword.setOnFocusChangeListener { v, hasFocus ->
            confirmPasswordFocusCheck(
                hasFocus
            )
        }
    }

    private fun confirmPasswordFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.editTextConfirmPasswordInputLayout.isErrorEnabled = true
            if (binding.editTextConfirmPassword.text.toString().length < 2) {
                binding.editTextConfirmPasswordInputLayout.error = "Password cannot be less than 4"
            } else {
                binding.editTextConfirmPasswordInputLayout.isErrorEnabled = false
            }

        }
    }

    private fun passwordFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.editTextPasswordInputLayout.isErrorEnabled = true
            if (binding.editTextPassword.text.toString().length < 2) {
                binding.editTextPasswordInputLayout.error = "Password cannot be less than 4"
            } else {
                binding.editTextPasswordInputLayout.isErrorEnabled = false
            }

        }
    }

    private fun phoneNumberFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.editTextPhoneNumberInputLayout.isErrorEnabled = true
            if (binding.editTextPhoneNumber.text.toString().length != 10) {
                binding.editTextPhoneNumberInputLayout.error = "Number should be 10 digit."
            } else {
                binding.editTextPhoneNumberInputLayout.isErrorEnabled = false
            }

        }
    }

    private fun emailNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.emailInputLayout.isErrorEnabled = true
            if (binding.email.text.toString().length < 2) {
                binding.emailInputLayout.error = "Email cannot be less than 5"
            } else {
                binding.emailInputLayout.isErrorEnabled = false
            }

        }
    }

    private fun lastNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.lastNameInputLayout.isErrorEnabled = true
            if (binding.lastName.text.toString().length < 2) {
                binding.lastNameInputLayout.error = "Lastname cannot be less than 3"
            } else {
                binding.lastNameInputLayout.isErrorEnabled = false
            }

        }
    }

    private fun firstNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.firstNameInputLayout.isErrorEnabled = true
            if (binding.firstName.text.toString().length < 2) {
                binding.firstNameInputLayout.error = "firstname cannot be less than 3"
            } else {
                binding.firstNameInputLayout.isErrorEnabled = false
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerUser() {
        val password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
        if (validateUserDetails(password, confirmPassword)) {
            saveToDb()
        }
    }


    private fun validateUserDetails(password: String, confirmPassword: String): Boolean {
        return if (!binding.checkboxTermsAndCondition.isChecked) {
            Toast.makeText(this, "Please agree terms and condition first.", Toast.LENGTH_LONG)
                .show()
            false
        } else
            password == confirmPassword
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToDb() {
        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val number = binding.editTextPhoneNumber.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val time = LocalDateTime.now().toString()
        firebaseDatabase.reference.child(Constants.USERS_DB_NAME)
            .child(Constants.USERS_DB_CHILD_NAME+time).setValue(User(firstName, lastName , email,number,password)).
                addOnCompleteListener(this@ActivitySignup, OnCompleteListener {task ->
                    if(task.isSuccessful){
                        startActivity(Intent(this@ActivitySignup,ActivityLogin::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@ActivitySignup,"Registration failed",Toast.LENGTH_LONG).show()
                    }
                })
    }

}