package com.example.faithconx.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityLoginBinding
import com.example.faithconx.helper.user.UserSession
import com.example.faithconx.helper.user.UserValidation
import com.example.faithconx.login.viewmodel.LoginAuthViewModel
import com.example.faithconx.main.ui.MainActivity
import com.example.faithconx.signup.ui.ActivitySignup
import com.example.faithconx.util.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ActivityLogin : AppCompatActivity(), View.OnClickListener {
    private val userSession = UserSession()
    private var isEmailCorrect = false
    private val userValidation = UserValidation()
    private lateinit var binding: ActivityLoginBinding
    private val loginAuthViewModel = LoginAuthViewModel()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var verificationId = ""
    private lateinit var authCredential: PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
        setOnClickListener()
        onTextChangeListener()
    }


    private fun onTextChangeListener() {
        binding.etEmail.addTextChangedListener { onEtEmailTextChange() }
        binding.etPassword.addTextChangedListener { onEtPasswordTextChange() }
    }

    private fun onEtPasswordTextChange() {
        if (userValidation.validatePassword(binding.etPassword, binding.tilPassword)
            && isEmailCorrect
        ) {
            binding.btnLogin.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_enable_color)
            binding.btnLogin.isEnabled = true
        } else if (binding.etPassword.text?.toString()?.isEmpty() == true) {
            binding.tilPassword.error = null
        } else {
            binding.btnLogin.backgroundTintList =
                resources.getColorStateList(R.color.loginbtn_disable_color)
            binding.btnLogin.isEnabled = false
        }
    }

    private fun onEtEmailTextChange() {
        if (binding.etEmail.text?.isEmpty() == true) {
            binding.tilEmail.error = null
            return
        }
        isEmailCorrect = userValidation.validateEmail(binding.etEmail, binding.tilEmail)
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
        binding.grpGmailLogin.visibility = View.GONE
        binding.grpGetOtp.visibility = View.VISIBLE

    }

    /**
     * First check if user inputs are well formatted and
     * on authentication if success redirect to Home fragment.
     *
     */
    fun onLoginClick(view: View) {
        binding.grpRemaining.visibility = View.GONE
        binding.grpGmailLogin.visibility = View.GONE
        binding.otpProgressBar.visibility = View.VISIBLE

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
                binding.otpProgressBar.visibility = View.GONE
                binding.tilPassword.error = Constants.EMAIL_OR_PASSWOD_WRONG_MSG
            }
        }
    }

    private fun validateUserCred(): Boolean {
        return binding.etEmail.text.toString().length < 8 && binding.etPassword.text.toString().length < 5
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
        //Register cpp with number
        binding.ccp.registerCarrierNumberEditText(binding.editTextPhoneNumber)
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(binding.ccp.fullNumberWithPlus)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    verificationId = otp //OTP ID
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@ActivityLogin, e.message, Toast.LENGTH_LONG)
                        .show()
                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        setVisibility(binding.grpSubmitOtp.id, View.VISIBLE)
    }

    fun onSubmitClick(view: View) {
        binding.editTextOtpSubmit.text.toString()?.let {
            if (it.isNotEmpty() && it.length == Constants.OTP_LENGTH) {
                //Progress Bar Enabled.
                binding.grpRemaining?.visibility = View.GONE
                binding.grpGetOtp?.visibility = View.GONE
                binding.grpSubmitOtp?.visibility = View.GONE
                binding.otpProgressBar?.visibility = View.VISIBLE
                authCredential = PhoneAuthProvider.getCredential(verificationId, it)
                signInWithPhoneAuthCredential(authCredential)
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
                    binding?.otpProgressBar?.visibility = View.GONE

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //Progress Bar Disabled.
                    binding?.otpProgressBar?.visibility = View.GONE
                    binding?.editTextOtpSubmitInputLayout?.isErrorEnabled = true
                    binding?.editTextOtpSubmitInputLayout?.error =
                        getString(R.string.wrongOtpEntered)

                }
            }
    }
}