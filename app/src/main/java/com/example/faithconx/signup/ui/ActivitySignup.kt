package com.example.faithconx.signup.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.databinding.ActivitySignupBinding
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.model.User
import com.example.faithconx.util.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ActivitySignup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onFocusChangeValidate()
        setOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        binding.btnContinue.setOnClickListener { registerUser() }
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
            if (binding.editTextConfirmPassword.text.toString().length < 5) {
                binding.editTextConfirmPasswordInputLayout.isHelperTextEnabled = true
                binding.editTextConfirmPasswordInputLayout.helperText="Password must be 6 digit or above."
            } else
                binding.editTextConfirmPasswordInputLayout.isHelperTextEnabled = false
        } else {
            binding.editTextConfirmPasswordInputLayout.isHelperTextEnabled = false
        }
    }

    private fun passwordFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.editTextPasswordInputLayout.isHelperTextEnabled = true
            if (binding.editTextPassword.text.toString().length < 5) {

                    binding.editTextPasswordInputLayout.helperText = "Password must be 6 digit or above."

            } else
                binding.editTextPasswordInputLayout.isHelperTextEnabled = false
        } else {
            binding.editTextPasswordInputLayout.isHelperTextEnabled = false
        }
    }

    private fun phoneNumberFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.editTextPhoneNumber.text.toString().length == 10) {
            binding.editTextPhoneNumberInputLayout.isHelperTextEnabled = true
                binding.editTextPhoneNumberInputLayout.helperText = "Number should be 10 digit."
            } else
                binding.editTextPhoneNumberInputLayout.isHelperTextEnabled = false
        } else {
            binding.editTextPhoneNumberInputLayout.isHelperTextEnabled = false
        }
    }

    private fun emailNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.email.text.toString().length < 4) {
            binding.emailInputLayout.isHelperTextEnabled = true
               binding.emailInputLayout.helperText = "Lastname cannot be less than 5."
            } else
                binding.emailInputLayout.isHelperTextEnabled = false
        } else {
            binding.emailInputLayout.isHelperTextEnabled = false
        }
    }


    private fun lastNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.lastName.text.toString().length < 4) {
            binding.lastNameInputLayout.isHelperTextEnabled = true
                setHelperText(binding.lastNameInputLayout, "Firstname cannot be less than 5.")
            } else
                binding.lastNameInputLayout.isHelperTextEnabled = false
        } else {
            binding.lastNameInputLayout.isHelperTextEnabled = false
        }
    }

    private fun firstNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.firstName.text.toString().length < 4) {
            binding.firstNameInputLayout.isHelperTextEnabled = true
                setHelperText(binding.firstNameInputLayout, "Firstname cannot be less than 5.")
            } else
                binding.firstNameInputLayout.isHelperTextEnabled = false
        } else {
            binding.firstNameInputLayout.isHelperTextEnabled = false
        }
    }

    private fun setHelperText(edittext: TextInputLayout, msg: String) {
        edittext.helperText = msg
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerUser() {
        val password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
        val email = binding.email.text.toString().trim()
        if(validateUserCred()) {
            Toast.makeText(this, "Check field and fill.", Toast.LENGTH_SHORT).show()
            return
        }else if (validateUserDetails(password, confirmPassword)) {
            loginUserWithEmailAndPassword(email, password)
        } else {
            Toast.makeText(this, "Password and confirm password doesn't match.", Toast.LENGTH_LONG)
                .show()
        }
    }
    private fun validateUserCred(): Boolean {
        return binding.email.text.toString().length < 8 && binding.editTextPassword.text.toString().length  < 5
                && binding.firstName.text.toString().length < 3  && binding.lastName.text.toString().length < 3
                && binding.editTextConfirmPassword.text.toString().length < 5
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
        binding.ccp.registerCarrierNumberEditText(binding.editTextPhoneNumber)
        val number = binding.ccp.fullNumberWithPlus
        val password = binding.editTextPassword.text.toString().trim()
        //Second store the user details
        firebaseAuth.uid?.let {
            firebaseDatabase.reference.child(Constants.USERS_DB_NAME)
                .child(it).setValue(User(firstName, lastName, email, number))
                .addOnCompleteListener(this@ActivitySignup, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ActivitySignup,
                            "Registration Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@ActivitySignup, ActivityLogin::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@ActivitySignup,
                            "Registration failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loginUserWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCustomToken:success")
                    saveToDb()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCustomToken:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
}