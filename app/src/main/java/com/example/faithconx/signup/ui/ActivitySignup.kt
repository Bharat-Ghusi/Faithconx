package com.example.faithconx.signup.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivitySignupBinding
import com.example.faithconx.helper.user.UserValidation
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.signup.helper.UserHelper
import com.example.faithconx.signup.viewmodel.AuthViewModel
import com.example.faithconx.signup.viewmodel.DatabaseViewModel
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ActivitySignup : AppCompatActivity(), View.OnClickListener,View.OnFocusChangeListener{
    companion object {
        private const val TAG = "ActivitySignup"
    }
    private var isFirstNameValid = false
    private var isLastNameValid = false
    private var isEmailValid = false
    private var isPhoneNumberValid = false
    private var isPasswordValid = false
    private var isConfirmPasswordValid = false

    private var uri: Uri? = null
    private lateinit var binding: ActivitySignupBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseViewModel = DatabaseViewModel()
    private val authModel = AuthViewModel()
    private val userValidation = UserValidation()
    private val userHelper = UserHelper()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
        setOnFocusChangeListener()
        setOnPhoneNumberChanges()
        onTextChangeListener()

    }

    private fun onTextChangeListener() {
        binding.etFirstName.addTextChangedListener { onFirstNameTextChange() }
//        binding.etEmail.addTextChangedListener { onLastNameTextChange() }
        binding.etPhoneNumber.addTextChangedListener { onPhoneNumberTextChange() }
        binding.etPassword.addTextChangedListener { onPasswordTextChange() }
        binding.etConfirmPassword.addTextChangedListener { onConfirmPasswordTextChange() }
    }

    private fun onFirstNameTextChange() {
        if(binding.etFirstName.text?.isEmpty() == true){
            binding.tilFirstName.error = null
            return
        }
        isFirstNameValid = userValidation.validateFirstName(binding.etFirstName, binding.tilFirstName)
    }

    private fun onPhoneNumberTextChange() {
        if(binding.etPhoneNumber.text?.isEmpty() == true){
            binding.tilPhoneNumber.error = null
            return
        }
        isPhoneNumberValid = userValidation.validateFirstName(binding.etPhoneNumber, binding.tilPhoneNumber)
    }

    private fun onPasswordTextChange() {
        if(binding.etPassword.text?.isEmpty() == true){
            binding.tilPassword.error = null
            return
        }
        isPasswordValid = userValidation.validateFirstName(binding.etPassword, binding.tilPassword)
    }

    private fun onConfirmPasswordTextChange() {
        if(userValidation.validateFirstName(binding.etConfirmPassword, binding.tilConfirmPassword)
            && isEmailValid && isFirstNameValid && isPhoneNumberValid && isPasswordValid){
            binding.btnContinue.backgroundTintList = resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnContinue.isEnabled= true
        }
        else if(binding.etConfirmPassword.text?.toString()?.isEmpty() == true){
            binding.tilConfirmPassword.error = null
        }
        else{
            binding.btnContinue.backgroundTintList = resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnContinue.isEnabled=false
        }
    }

    private fun setOnPhoneNumberChanges() {
        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        binding.ccp.setPhoneNumberValidityChangeListener {
            if(!it){
                binding.etPhoneNumber.filters =arrayOf<InputFilter>()
            }else{
                // Create an InputFilter to constrain the length limit
                val filters = arrayOf<InputFilter>(InputFilter.LengthFilter( binding.etPhoneNumber.text.toString().length))
                binding.etPhoneNumber.filters = filters
                binding.etPhoneNumber.error = null
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        binding.btnClose.setOnClickListener(this@ActivitySignup)
        binding.btnContinue.setOnClickListener(this@ActivitySignup)
        binding.civCamera.setOnClickListener(this@ActivitySignup)
        binding.ccp.setOnCountryChangeListener { onCountryChange() }

    }

    private fun onCountryChange() {
        val filters = arrayOf<InputFilter>()
        binding.etPhoneNumber.filters = filters
       binding. etPhoneNumber.setText("")
    }

    private fun setOnFocusChangeListener() {
        binding.etFirstName.onFocusChangeListener = this@ActivitySignup
        binding.etEmail.onFocusChangeListener = this@ActivitySignup
        binding.etPhoneNumber.onFocusChangeListener = this@ActivitySignup
        binding.etPassword.onFocusChangeListener = this@ActivitySignup
        binding.etConfirmPassword.onFocusChangeListener = this@ActivitySignup
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
      when(view?.id){
          R.id.etFirstName -> userHelper.etFirstNameFocusChange(hasFocus,binding.tilFirstName,binding.etFirstName)
          R.id.etEmail ->userHelper. etEmailFocusChange(hasFocus,binding.tilEmail,binding.etEmail)
          R.id.etPhoneNumber -> onEtPhoneNumberFocusChange()
          R.id.etPassword ->userHelper. etPasswordFocusChange(hasFocus,binding.tilPassword,binding.etPassword)
          R.id.etConfirmPassword -> userHelper.etConfirmPasswordFocusChange(hasFocus,binding.tilConfirmPassword,binding.etConfirmPassword,binding.etPassword)
      }
    }

    /**
     * On focus change of phone number edittext it will check
     * if number is valid or not and set the error message.
     */
    private fun onEtPhoneNumberFocusChange() {
        binding.etPhoneNumber.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (!binding.ccp.isValidFullNumber) {
                    binding.tilPhoneNumber.error = Constants.INVALID_NUMBER_MSG
                }else{
                    binding.tilPhoneNumber.error = null
                }


            } else if (!binding.ccp.isValidFullNumber) {
                binding.tilPhoneNumber.error = Constants.INVALID_NUMBER_MSG

            }
            else{
                binding.tilPhoneNumber.error = null
            }
        }
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
        val email = binding.etEmail.text.toString().trim()


        //Validate user details
        if(!userValidation.validateFirstName(binding.etFirstName,binding.tilFirstName) ){
            binding.tilFirstName.requestFocus()

            binding.tilEmail.error=null
            binding.tilPhoneNumber.error=null
            binding.tilPassword.error=null
            binding.tilConfirmPassword.error=null

            return
        }
        else if(!userValidation.validateEmail(binding.etEmail,binding.tilEmail)){
            binding.tilEmail.requestFocus()

            binding.tilFirstName.error=null
            binding.tilPhoneNumber.error=null
            binding.tilPassword.error=null
            binding.tilConfirmPassword.error=null
            return
        }

        else if(!binding.ccp.isValidFullNumber){
            binding.tilPhoneNumber.error = Constants.INVALID_NUMBER_MSG
            binding.tilPhoneNumber.requestFocus()

            binding.tilEmail.error=null
            binding.tilFirstName.error=null
            binding.tilPassword.error=null
            binding.tilConfirmPassword.error=null
            return
        }
         else if(!userValidation.validatePassword(binding.etPassword,binding.tilPassword) ){
            binding.tilPassword.requestFocus()

            binding.tilEmail.error=null
            binding.tilPhoneNumber.error=null
            binding.tilFirstName.error=null
            binding.tilConfirmPassword.error=null
            return
        }
         else if(!userValidation.validateConfirmPassword(binding.etPassword, binding.etConfirmPassword,binding.tilConfirmPassword)){
            binding.tilConfirmPassword.requestFocus()

            binding.tilEmail.error=null
            binding.tilPhoneNumber.error=null
            binding.tilPassword.error=null
            binding.tilFirstName.error=null
            return
        }

//      Checkbox validation.
        else if (!validateCheckbox()) {
            binding.checkboxTermsAndCondition.requestFocus()
            return
        }
        //Everything is right good to go.
        else
        loginUserWithEmailAndPassword(email, password)
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

    //Checkbox validation
    private fun validateCheckbox():Boolean{
        if (!binding.checkboxTermsAndCondition.isChecked) {
            binding.checkboxTermsAndCondition.error = Constants.TERMS_AND_CONDITION_MSG
            Toast.makeText(this,Constants.TERMS_AND_CONDITION_MSG,Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            binding.checkboxTermsAndCondition.error =null
            return true
        }

    }


    /**
     * Store user profile details to Firebase realtime DB
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToDb() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()


        val number = binding.ccp.fullNumberWithPlus
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




    private fun uploadProfileImage() {

    }

}