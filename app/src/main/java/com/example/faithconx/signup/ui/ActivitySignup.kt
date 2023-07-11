package com.example.faithconx.signup.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivitySignupBinding
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.signup.viewmodel.AuthViewModel
import com.example.faithconx.signup.viewmodel.DatabaseViewModel
import com.example.faithconx.util.Constants
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ActivitySignup : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val TAG = "ActivitySignup"
    }

    private lateinit var binding: ActivitySignupBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseViewModel = DatabaseViewModel()
    private val authModel = AuthViewModel()

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
        binding.btnClose.setOnClickListener(this@ActivitySignup)
        binding.btnContinue.setOnClickListener(this@ActivitySignup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnClose -> redirectToLoginScreen()
            R.id.btnContinue -> registerUser()
        }
    }

    //    1: redirect to login screen on click of close btn
    private fun redirectToLoginScreen() {
        startActivity(Intent(this, ActivityLogin::class.java))
        finish()
    }

    /**
     * This method will check if fields are at least contain something
     */
    private fun onFocusChangeValidate() {
        binding.etFirstName.setOnFocusChangeListener { v, hasFocus -> firstNameFocusCheck(hasFocus) }
        binding.etLastName

            .setOnFocusChangeListener { v, hasFocus -> lastNameFocusCheck(hasFocus) }
        binding.etEmail.setOnFocusChangeListener { v, hasFocus -> emailNameFocusCheck(hasFocus) }
        binding.etPhoneNumber.setOnFocusChangeListener { v, hasFocus ->
            phoneNumberFocusCheck(
                hasFocus
            )
        }
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            passwordFocusCheck(
                hasFocus
            )
        }
        binding.etConfirmPassword.setOnFocusChangeListener { v, hasFocus ->
            confirmPasswordFocusCheck(
                hasFocus
            )
        }
    }

    private fun confirmPasswordFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.etConfirmPassword.text.toString().length < 5) {
                binding.tilConfirmPassword.isHelperTextEnabled = true
                binding.tilConfirmPassword.helperText = "Password must be 6 digit or above."
            } else
                binding.tilConfirmPassword.isHelperTextEnabled = false
        } else {
            binding.tilConfirmPassword.isHelperTextEnabled = false
        }
    }

    private fun passwordFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.tilPassword.isHelperTextEnabled = true
            if (binding.etPassword.text.toString().length < 5) {

                binding.tilPassword.helperText = "Password must be 6 digit or above."

            } else
                binding.tilPassword.isHelperTextEnabled = false
        } else {
            binding.tilPassword.isHelperTextEnabled = false
        }
    }

    private fun phoneNumberFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.etPhoneNumber.text.toString().length == 10) {
                binding.tilPhoneNumber.isHelperTextEnabled = true
                binding.tilPhoneNumber.helperText = "Number should be 10 digit."
            } else
                binding.tilPhoneNumber.isHelperTextEnabled = false
        } else {
            binding.tilPhoneNumber.isHelperTextEnabled = false
        }
    }

    private fun emailNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.etEmail.text.toString().length < 4) {
                binding.tilEmail.isHelperTextEnabled = true
                binding.tilEmail.helperText = "Lastname cannot be less than 5."
            } else
                binding.tilEmail.isHelperTextEnabled = false
        } else {
            binding.tilEmail.isHelperTextEnabled = false
        }
    }


    private fun lastNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.etLastName.text.toString().length < 4) {
                binding.tilLastName.isHelperTextEnabled = true
                setHelperText(binding.tilLastName, "Firstname cannot be less than 5.")
            } else
                binding.tilLastName.isHelperTextEnabled = false
        } else {
            binding.tilLastName.isHelperTextEnabled = false
        }
    }

    private fun firstNameFocusCheck(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.etFirstName.text.toString().length < 4) {
                binding.tilFirstName.isHelperTextEnabled = true
                setHelperText(binding.tilFirstName, "Firstname cannot be less than 5.")
            } else
                binding.tilFirstName.isHelperTextEnabled = false
        } else {
            binding.tilFirstName.isHelperTextEnabled = false
        }
    }

    private fun setHelperText(edittext: TextInputLayout, msg: String) {
        edittext.helperText = msg
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerUser() {
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        if (validateUserCred()) {
            Toast.makeText(this, "Check field and fill.", Toast.LENGTH_SHORT).show()
            return
        } else if (validateUserDetails(password, confirmPassword)) {
            loginUserWithEmailAndPassword(email, password)
        } else {
            Toast.makeText(this, "Password and confirm password doesn't match.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateUserCred(): Boolean {
        return binding.etEmail.text.toString().length < 8 && binding.etPassword.text.toString().length < 5
                && binding.etFirstName.text.toString().length < 3 && binding.etLastName.text.toString().length < 3
                && binding.etConfirmPassword.text.toString().length < 5
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
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        val number = binding.ccp.fullNumberWithPlus
        val password = binding.etPassword.text.toString().trim()
        //store the user details
        databaseViewModel.saveUserDetailsToDb(firstName, lastName, email, number, firebaseAuth)
        databaseViewModel.getDataSavingState().observe(this) { state ->
            if (state) {
                //Data save successfully
                Log.i(TAG, Constants.SUCCESS_MSG_OF_SAVING_USER_DATA)
                startActivity(Intent(this@ActivitySignup, ActivityLogin::class.java))
                finish()
            } else {
                //Failed to save data
                Log.i(TAG, Constants.FAILURE_MSG_OF_SAVING_USER_DATA)
            }
        }
//


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loginUserWithEmailAndPassword(email: String, password: String) {
        authModel.register(email, password)
        // User is already logged in,
        if (firebaseAuth.currentUser != null) {
            Log.i(TAG,firebaseAuth.currentUser.toString())
            Toast.makeText(this, Constants.USER_ALREADY_LOGGEDIN_MSG, Toast.LENGTH_LONG).show()
            return
        }
        authModel.getAuthenticationState().observe(
            this
        ) {
            if (it) {
                saveToDb()
            } else {
                Toast.makeText(this,Constants.AUHTENTICATION_FAILED_MSG,Toast.LENGTH_LONG).show()
            }
        }
    }


}