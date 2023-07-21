package com.example.faithconx.signup.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivitySignupBinding
import com.example.faithconx.helper.user.UserValidation
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.main.ui.MainActivity
import com.example.faithconx.signup.helper.MediaPermission
import com.example.faithconx.signup.viewmodel.AuthViewModel
import com.example.faithconx.signup.viewmodel.DatabaseViewModel
import com.example.faithconx.signup.viewmodel.ImageStorageViewModel
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ActivitySignup : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener {
    companion object {
        private const val TAG = "ActivitySignup"
    }

    private val imageDatabaseViewModel = ImageStorageViewModel()
    private var imageUrl: Uri? = null
    private val mediaPermission = MediaPermission()
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
    private lateinit var  userValidation:UserValidation


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userValidation = UserValidation(this)
        setOnClickListener()
        setOnPhoneNumberChanges()
        onTextChangeListener()
    }

    private fun onTextChangeListener() {
        binding.etFirstName.addTextChangedListener { onFirstNameTextChange() }
        binding.etEmail.addTextChangedListener { onEmailTextChange() }
        binding.etPhoneNumber.addTextChangedListener { onPhoneNumberTextChange() }
        binding.etPassword.addTextChangedListener { onPasswordTextChange() }
        binding.etConfirmPassword.addTextChangedListener { onConfirmPasswordTextChange() }
    }



    private fun onFirstNameTextChange() {
        isFirstNameValid =
            userValidation.validateFirstName(binding.etFirstName, binding.tilFirstName)
        if(isFirstNameValid &&  isConfirmPasswordValid  && isEmailValid && isPhoneNumberValid && isPasswordValid ){
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnContinue.isEnabled = true
            binding.tilPassword.error = null
        }  // either password is invalid or confirm password is invalid
        else{
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnContinue.isEnabled = false
        }

    }

    private fun onEmailTextChange() {

        isEmailValid = userValidation.validateEmail(binding.etEmail, binding.tilEmail)

        if(isEmailValid &&  isConfirmPasswordValid  && isFirstNameValid && isPhoneNumberValid && isPasswordValid ){
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnContinue.isEnabled = true
            binding.tilPassword.error = null
        }  // either password is invalid or confirm password is invalid
        else{
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnContinue.isEnabled = false
        }


    }

    private fun onPhoneNumberTextChange() {

        isPhoneNumberValid = userValidation.validatePhoneNumber(
            binding.etPhoneNumber, binding.tilPhoneNumber, binding.ccp
        )

        if(isPhoneNumberValid &&  isConfirmPasswordValid  && isFirstNameValid && isEmailValid && isPasswordValid ){
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnContinue.isEnabled = true
            binding.tilPassword.error = null
        }  // either password is invalid or confirm password is invalid
        else{
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnContinue.isEnabled = false
        }

    }

    private fun onPasswordTextChange() {
        isPasswordValid = userValidation.validatePassword(binding.etPassword, binding.tilPassword)
        if(! isPasswordValid && !isConfirmPasswordValid){

        }
        if( isPasswordValid &&  isConfirmPasswordValid  && isFirstNameValid && isEmailValid && isPhoneNumberValid  ){
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnContinue.isEnabled = true
            binding.tilPassword.error = null
        }
        // either password is invalid or confirm password is invalid
        else{
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnContinue.isEnabled = false
        }
    }

    private fun onConfirmPasswordTextChange() {
        isConfirmPasswordValid = userValidation.validateConfirmPassword(
            binding.etPassword, binding.etConfirmPassword, binding.tilConfirmPassword
        )
        if(! isConfirmPasswordValid &&  binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()){
            binding.tilConfirmPassword.error = getString(R.string.password_doesn_t_match)
        }

        else if(isEmailValid && isFirstNameValid && isPhoneNumberValid && isPasswordValid){
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnContinue.isEnabled = true
            binding.tilConfirmPassword.error = null
        }
        //Something is wrong
        else{
            binding.btnContinue.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnContinue.isEnabled = false

        }
    }

    private fun setOnPhoneNumberChanges() {
        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        binding.ccp.setPhoneNumberValidityChangeListener {
            if (!it) {
                binding.etPhoneNumber.filters = arrayOf<InputFilter>()
            } else {
                // Create an InputFilter to constrain the length limit
                val filters =
                    arrayOf<InputFilter>(InputFilter.LengthFilter(binding.etPhoneNumber.text.toString().length))
                binding.etPhoneNumber.filters = filters
                binding.etPhoneNumber.error = null
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        binding.btnClose.setOnClickListener(this@ActivitySignup)
        binding.btnContinue.setOnClickListener(this@ActivitySignup)
        binding.civEditProfileImg.setOnClickListener(this@ActivitySignup)
        binding.ccp.setOnCountryChangeListener { onCountryChange() }

        //Focus click listener
        binding.etFirstName.onFocusChangeListener = this@ActivitySignup
        binding.etEmail.onFocusChangeListener = this@ActivitySignup
        binding.etPhoneNumber.onFocusChangeListener = this@ActivitySignup
        binding.etPassword.onFocusChangeListener = this@ActivitySignup
        binding.etConfirmPassword.onFocusChangeListener = this@ActivitySignup

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnClose -> redirectToLoginScreen()
            R.id.btnContinue -> onContinueClick(view)
            R.id.civ_editProfileImg -> onEditProfileImgClick(view)
        }
    }

    /**
     * On country change
     */

    @RequiresApi(Build.VERSION_CODES.S)
    private fun onCountryChange() {

        val filters = arrayOf<InputFilter>()
        binding.etPhoneNumber.filters = filters
        binding.etPhoneNumber.setText("")
    }



    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        when (view?.id) {
            R.id.etFirstName -> {
                if(!hasFocus){
                    onFirstNameTextChange()
                }
            }


            R.id.etEmail ->  {
                if(!hasFocus){
                    onEmailTextChange()
                }
            }

            R.id.etPhoneNumber -> onEtPhoneNumberFocusChange()

            R.id.etPassword ->  {
                if(!hasFocus){
                    onPasswordTextChange()
                }
            }

            R.id.etConfirmPassword ->  {
                if(!hasFocus){
                    onConfirmPasswordTextChange()
                }
            }

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
                } else {
                    binding.tilPhoneNumber.error = null
                }


            } else if (!binding.ccp.isValidFullNumber) {
                binding.tilPhoneNumber.error = Constants.INVALID_NUMBER_MSG

            } else {
                binding.tilPhoneNumber.error = null
            }
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


//      Checkbox validation.
        if (!validateCheckbox()) {
            binding.checkboxTermsAndCondition.requestFocus()
            return
        }
        //Everything is right good to go.
        else registerUserWithEmailAndPassword(email, password)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerUserWithEmailAndPassword(email: String, password: String) {
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
            //User has successfully register with email and password
            if (it) {
                //save to DB
                saveToDb()
            }
            //User registration failed
            else {
                Toast.makeText(this, Constants.AUHTENTICATION_FAILED_MSG, Toast.LENGTH_LONG).show()
            }
        }
    }

    //Checkbox validation
    private fun validateCheckbox(): Boolean {
        if (!binding.checkboxTermsAndCondition.isChecked) {
            binding.checkboxTermsAndCondition.error = Constants.TERMS_AND_CONDITION_MSG
            Toast.makeText(this, Constants.TERMS_AND_CONDITION_MSG, Toast.LENGTH_SHORT).show()
            return false
        } else {
            binding.checkboxTermsAndCondition.error = null
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
//        upload image first
//     uploadProfileImage()

        //Observer
        filePathUri?.let {
            imageDatabaseViewModel.uploadProfileImage(filePathUri)
            imageDatabaseViewModel.isImageSaved().observe(this, Observer { isImageSaved ->
                if (isImageSaved) {
                    imageDatabaseViewModel.getDownloadedImageUri()
                        .observe(this@ActivitySignup, Observer { uri ->
                            saveUserCredential(firstName, lastName, email, number, uri.toString())
                        })
                }
                //False means save user input without profiles.
                else {
                    Log.e("TAG", "Image upload failed.")
                }
            })

        } ?: saveUserCredential(firstName, lastName, email, number, "")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadProfileImage() {
        val storageReference =
            FirebaseStorage.getInstance().getReference(Constants.PROFILE_IMAGES_ROOT_KEY)
                .child(Constants.IMAGE_URL_PREFIX + firebaseAuth.currentUser?.uid)
        filePathUri?.let { filePathUri ->
            storageReference.putFile(filePathUri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("TAG", "Image uploaded successfully.")
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            //Save complete user cred
                            saveUserCredential(
                                binding.etFirstName.text.toString().trim(),
                                binding.etLastName.text.toString().trim(),
                                binding.etEmail.text.toString().trim(),
                                binding.ccp.fullNumberWithPlus, uri.toString()
                            )

                            imageUrl = uri
                            Log.i("TAG", "Downloaded img uri successfully: ${uri.toString()}")
                        }
                    } else {
                        //Save complete user cred
                        saveUserCredential(
                            binding.etFirstName.text.toString().trim(),
                            binding.etLastName.text.toString().trim(),
                            binding.etEmail.text.toString().trim(),
                            binding.ccp.fullNumberWithPlus, uri.toString()
                        )

                        Log.i("TAG", "Image upload failed.")
                        Toast.makeText(
                            this@ActivitySignup,
                            "Image updation failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun saveUserCredential(
        firstName: String,
        lastName: String,
        email: String,
        fullNumberWithPlus: String,
        imageUrl: String
    ) {


        //store the user details
        imageUrl?.toString()?.let { imageUrl ->
            databaseViewModel.saveUserDetailsToDb(
                firstName,
                lastName,
                email,
                fullNumberWithPlus,
                imageUrl,
                firebaseAuth
            )
        } ?: databaseViewModel.saveUserDetailsToDb(
            firstName,
            lastName,
            email,
            fullNumberWithPlus,
            "",
            firebaseAuth
        )
        databaseViewModel.getDataSavingState().observe(this) { state ->
            if (state) {
                //Data save successfully
                Log.i(ActivitySignup.TAG, Constants.SUCCESS_MSG_OF_SAVING_USER_DATA)
                startActivity(Intent(this@ActivitySignup, MainActivity::class.java))
                finish()
            } else {
                //Failed to save data
                Log.i(ActivitySignup.TAG, Constants.FAILURE_MSG_OF_SAVING_USER_DATA)
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onEditProfileImgClick(view: View) {
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) READ_MEDIA_IMAGES
            else READ_EXTERNAL_STORAGE
        val permissions = arrayOf(readImagePermission)
//        Permission granted
        if (mediaPermission.isPermissionGranted(this, arrayOf(readImagePermission))) {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show()
            browseProfileImage()
        }
//        Permission not granted then request for permission
        else {
            mediaPermission.requestPermission(this, permissions, Constants.MEDIA_PERMISSION_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.MEDIA_PERMISSION_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show()
                browseProfileImage()
            } else
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * . Use registerForActivityResult(ActivityResultContract, ActivityResultCallback)
     * passing in a StartActivityForResult object for the ActivityResultContract.
     */
    private var filePathUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                filePathUri = result.data?.data
                binding.civProfile.setImageURI(filePathUri)

            }
        }

    /**
     * Browse Image from external storage.
     */


    @RequiresApi(Build.VERSION_CODES.O)
    private fun browseProfileImage() {
        resultLauncher.launch(Intent(Intent.ACTION_PICK).setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

}