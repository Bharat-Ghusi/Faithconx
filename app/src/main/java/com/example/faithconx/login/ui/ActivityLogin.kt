package com.example.faithconx.login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
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
import com.example.faithconx.view.ActivityOtpVerification
import kotlin.system.exitProcess

class ActivityLogin : AppCompatActivity(), View.OnClickListener {
    private val userSession = UserSession()
    private var isEmailCorrect= false
    private val userValidation = UserValidation()
    private lateinit var binding: ActivityLoginBinding
    private val loginAuthViewModel = LoginAuthViewModel()
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
      if( userValidation.validatePassword(binding.etPassword,binding.tilPassword)
                &&  isEmailCorrect ){
          binding.btnLogin.backgroundTintList = resources.getColorStateList(R.color.loginbtn_enable_color)
          binding.btnLogin.isEnabled= true
      }
        else if(binding.etPassword.text?.toString()?.isEmpty() == true){
            binding.tilPassword.error = null
      }
        else{
          binding.btnLogin.backgroundTintList = resources.getColorStateList(R.color.loginbtn_disable_color)
          binding.btnLogin.isEnabled=false
      }
    }

    private fun onEtEmailTextChange() {
        if(binding.etEmail.text?.isEmpty() == true){
            binding.tilEmail.error = null
            return
        }
        isEmailCorrect = userValidation.validateEmail(binding.etEmail,binding.tilEmail)
    }


    private fun setOnClickListener() {
        //Change to VIew.onClickListener()
        //use when and scope function
        binding.btnLoginWithPhoneNumber.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvCreateOne.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLoginWithPhoneNumber -> onClickLoginWithPhoneNumber(view)
            R.id.btnLogin -> onLoginClick(view)
            R.id.tvCreateOne -> onCreateOneClick(view)
            R.id.ivClose ->  onCloseClick()
        }
    }

    private fun onCloseClick() {
        finish()
    }


    /**
     * Open OTP Activity.
     */
    fun onClickLoginWithPhoneNumber(view: View) {
        startActivity(Intent(this, ActivityOtpVerification::class.java))
    }

    /**
     * First check if user inputs are well formatted and
     * on authentication if success redirect to Home fragment.
     *
     */
    fun onLoginClick(view: View) {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        //Authenticate user
        loginAuthViewModel.loginUser(email, password)
        //Redirect to home screen if authentication successful
        loginAuthViewModel.getAuthenticationState().observe(this){ isSuccess ->
            if(isSuccess){
                userSession.createSession(this)
                userSession.setUser(com.example.faithconx.model.UserSession(email,true))
                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                finish()
            }
            //Email or password is wrong
            else{
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


}