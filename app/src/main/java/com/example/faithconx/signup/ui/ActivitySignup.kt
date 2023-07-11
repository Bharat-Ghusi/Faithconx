package com.example.faithconx.signup.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivitySignupBinding
import com.example.faithconx.helper.user.UserValidation
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

    private var uri: Uri? = null
    private lateinit var binding: ActivitySignupBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseViewModel = DatabaseViewModel()
    private val authModel = AuthViewModel()
    private val userValidation = UserValidation()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        binding.btnClose.setOnClickListener(this@ActivitySignup)
        binding.btnContinue.setOnClickListener(this@ActivitySignup)
        binding.civCamera.setOnClickListener(this@ActivitySignup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnClose -> redirectToLoginScreen()
            R.id.btnContinue -> onContinueClick(view)
        }
    }


    //    1: redirect to login screen on click of close btn
    private fun redirectToLoginScreen() {
        startActivity(Intent(this, ActivityLogin::class.java))
        finish()
    }


    /**
     * Register user
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onContinueClick(view: View?) {
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        //Validate user details
        if(!userValidation.validateFirstName(binding.etFirstName) && !userValidation.validateLastName(binding.etLastName)
            && !userValidation.validateEmail(binding.etEmail) &&  !userValidation.validatePhoneNumber(binding.etPhoneNumber)
            && !userValidation.validatePassword(binding.etPassword) && !userValidation.validateConfirmPassword(binding.etPassword, binding.etConfirmPassword)){
            return
        }

        if (!binding.checkboxTermsAndCondition.isChecked) {
            Toast.makeText(this, Constants.TERMS_AND_CONDITION_MSG , Toast.LENGTH_LONG)
                .show()
            return
        }
        else
        loginUserWithEmailAndPassword(email, password)
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
            Log.i(TAG, firebaseAuth.currentUser.toString())
            Toast.makeText(this, Constants.USER_ALREADY_LOGGEDIN_MSG, Toast.LENGTH_LONG).show()
            return
        }
        authModel.getAuthenticationState().observe(
            this
        ) {
            if (it) {
                //Upload image first
                uploadProfileImage()
                //save to DB
                saveToDb()
            } else {
                Toast.makeText(this, Constants.AUHTENTICATION_FAILED_MSG, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun uploadProfileImage() {

    }



    /**
     * User details validation
     */


}