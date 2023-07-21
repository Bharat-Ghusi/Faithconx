package com.example.faithconx.login.ui

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityLoginBinding
import com.example.faithconx.helper.user.UserSession
import com.example.faithconx.helper.user.UserValidation
import com.example.faithconx.login.viewmodel.LoginAuthViewModel
import com.example.faithconx.login.viewmodel.PhoneAuthViewModel
import com.example.faithconx.main.ui.MainActivity
import com.example.faithconx.signup.ui.ActivitySignup
import com.example.faithconx.util.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ActivityLogin : AppCompatActivity(), View.OnClickListener, OnFocusChangeListener {
    private var otpScreenFlag = true
    private var phoneAuthViewModel: PhoneAuthViewModel? = null
    private val userSession = UserSession()
    private var isPasswordCorrect = false
    private var isEmailCorrect = false
    private lateinit var userValidation :UserValidation
    private lateinit var binding: ActivityLoginBinding
    private val loginAuthViewModel = LoginAuthViewModel()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var verificationId = ""
    private lateinit var authCredential: PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userValidation = UserValidation(this)
        setOnClickListener()
        setOnFocusListener()
        onTextChangeListener()
    }

    private fun setOnFocusListener() {
        binding.etPassword.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        when (view?.id) {
            R.id.etPassword -> onPasswordFocusChange(hasFocus)
            R.id.etEmail -> onEmailFocusChange(hasFocus)
        }
    }

    private fun onPasswordFocusChange(hasFocus: Boolean) {
        if (!hasFocus) {

            if (binding.etPassword.text?.isEmpty() == true) {
                binding.tilPassword.error = getString(R.string.field_cannot_be_empty)
            } else {
                binding.tilPassword.error = getString(R.string.empty)
            }
        }
    }

    private fun onEmailFocusChange(hasFocus: Boolean) {
        if (!hasFocus) {
            if (binding.etEmail.text?.isEmpty() == true) {
                binding.tilEmail.error = getString(R.string.field_cannot_be_empty)
            }
        }
    }

    private fun onTextChangeListener() {
        binding.etEmail.addTextChangedListener { onEtEmailTextChange() }
        binding.etPassword.addTextChangedListener { onEtPasswordTextChange() }
    }

    private fun onEtPasswordTextChange() {
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.tilPassword.error = getString(R.string.password_cannot_be_empty)
            binding.btnLogin.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnLogin.isEnabled = false
            isPasswordCorrect = false
        }
//        isEmailCorrect == true and password is not empty
        else if (isEmailCorrect) {
            binding.tilPassword.error = getString(R.string.empty)
            isPasswordCorrect = true
            binding.btnLogin.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnLogin.isEnabled = true

        }
//        isEmailCorrect == false && password is not empty
        else
            binding.tilPassword.error = getString(R.string.empty)
        isPasswordCorrect = true


    }

    private fun onEtEmailTextChange() {
        isEmailCorrect = userValidation.validateEmail(binding.etEmail, binding.tilEmail)
        if (!isEmailCorrect) {
            binding.btnLogin.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnLogin.isEnabled = false
        } else if (isPasswordCorrect) {
            binding.btnLogin.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnLogin.isEnabled = true
        }


    }


    private fun setOnClickListener() {
        //Change to VIew.onClickListener()
        //use when and scope function
        binding.btnLoginWithPhoneNumber.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvCreateOne.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.btnGetOtp.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLoginWithPhoneNumber -> onClickLoginWithPhoneNumber(view)
            R.id.btnLogin -> onLoginClick(view)
            R.id.tvCreateOne -> onCreateOneClick(view)
            R.id.ivClose -> onCloseClick()
            R.id.btnGetOtp -> onGetOtpClick(view)
            R.id.btnSubmit -> onSubmitClick(view)
        }
    }

    private fun onCloseClick() {
        finish()
    }


    /**
     * Open OTP Activity.
     */
    fun onClickLoginWithPhoneNumber(view: View) {
       if(otpScreenFlag){
           otpScreenFlag = false
           binding.grpGmailLogin.visibility = View.GONE
           binding.grpGetOtp.visibility = View.VISIBLE
           binding.btnLoginWithPhoneNumber.text = getString(R.string.login_with_email)
       }else{
           otpScreenFlag = true
           binding.grpGmailLogin.visibility = View.VISIBLE
           binding.grpGetOtp.visibility = View.GONE
           binding.btnLoginWithPhoneNumber.text = getString(R.string.loginWithPhoneNumber)
       }

    }
    private fun enableKeyboard(flag:Boolean){
        try {
            this.currentFocus?.let {
               if(flag){

               }else{
                   val inputMethodManager =
                       getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                   inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken,0)
               }

            }
        }catch (e:Exception){e.printStackTrace()}
    }

    /**
     * First check if user inputs are well formatted and
     * on authentication if success redirect to Home fragment.
     *
     */
    fun onLoginClick(view: View) {
        enableKeyboard(false)
        //HIDE all view
        binding.grpRemaining.visibility = View.GONE
        binding.grpGmailLogin.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        //Authenticate user
        loginAuthViewModel.loginUser(email, password)
        //Redirect to home screen if authentication successful
        loginAuthViewModel.getAuthenticationState().observe(this) { isSuccess ->
            if (isSuccess) {
                userSession.createSession(this)
                userSession.setUser(com.example.faithconx.model.UserSession(email, true))
                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                finish()
            }
            //Email or password is wrong
            else {
                binding.grpRemaining.visibility = View.VISIBLE
                binding.grpGmailLogin.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.tilPassword.error = getString(R.string.email_or_password_is_wrong)
            }
        }
    }


    fun onCreateOneClick(view: View) {
        startActivity(Intent(this, ActivitySignup::class.java))
        finish()
    }

    /**
     * OTP LOGIN
     */

    //    Send a verification code to the user's phone
    fun onGetOtpClick(view: View) {
        //hide keyboard
        enableKeyboard(false)
        binding.grpRemaining.visibility = View.GONE
        binding.grpGetOtp.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        phoneAuthViewModel = PhoneAuthViewModel(this)
        binding.ccp.registerCarrierNumberEditText(binding.editTextPhoneNumber)
        phoneAuthViewModel?.getOtp(binding.ccp.fullNumberWithPlus)
        phoneAuthViewModel?.getLoggedInState()?.observe(this, Observer {logInState ->
            if(logInState){
                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                finish()
            }
            //Otp SEnt failed
            else{
                binding.progressBar.visibility = View.GONE
                binding.grpRemaining.visibility = View.VISIBLE
                binding.grpGetOtp.visibility =View.VISIBLE
            }
        })
        phoneAuthViewModel?.getOtpVerificationId()?.observe(this@ActivityLogin, Observer { otp ->
            binding.progressBar.visibility = View.GONE
            binding.grpRemaining.visibility =View.VISIBLE
            binding.grpSubmitOtp.visibility =View.VISIBLE
            verificationId = otp

        })


////////////////////
//        val options = PhoneAuthOptions.newBuilder()
//            .setPhoneNumber(binding.ccp.fullNumberWithPlus)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                    super.onCodeSent(otp, p1)
//                    verificationId = otp //OTP ID
//                }
//
//                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    signInWithPhoneAuthCredential(credential)
//                }
//
//                override fun onVerificationFailed(e: FirebaseException) {
//                    Toast.makeText(this@ActivityLogin, e.message, Toast.LENGTH_LONG)
//                        .show()
//                }
//
//            }).build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//
//        setVisibility(binding.grpSubmitOtp.id, View.VISIBLE)
    }

    fun onSubmitClick(view: View) {

        //hide keyboard
        enableKeyboard(false)
        ///////////
        binding.editTextOtpSubmit.text.toString()?.let {
            if (it.isNotEmpty() && it.length == Constants.OTP_LENGTH) {
                //Progress Bar Enabled.
                binding.grpRemaining.visibility = View.GONE
                binding.grpGetOtp.visibility = View.GONE
                binding.grpSubmitOtp.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                authCredential = PhoneAuthProvider.getCredential(verificationId, it)
               phoneAuthViewModel?.signInWithPhoneAuthCredential(authCredential)
            }
        }

    }

    private fun setVisibility(id: Int, visibility: Int) {
        when (id) {
            R.id.grpGetOtp -> binding.grpGetOtp.visibility = visibility
            R.id.grpSubmitOtp -> binding.grpSubmitOtp.visibility = visibility
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Progress Bar Disabled.
//                    binding?.otpProgressBar?.visibility = View.GONE

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //Progress Bar Disabled.
//                    binding?.otpProgressBar?.visibility = View.GONE
                    binding?.editTextOtpSubmitInputLayout?.isErrorEnabled = true
                    binding?.editTextOtpSubmitInputLayout?.error =
                        getString(R.string.wrongOtpEntered)

                }
            }
    }


}