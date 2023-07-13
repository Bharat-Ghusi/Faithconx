package com.example.faithconx.splash.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.faithconx.R
import com.example.faithconx.helper.user.UserSession
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.main.ui.MainActivity
import com.example.faithconx.util.Constants

class SplashActivity : AppCompatActivity() {
    companion object{
        private const val SPLASH_SCREEN_DURATION = 1000L
    }
    private  val userSession = UserSession()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Initialize session first
        createSplashScreen()
    }

    private fun createSplashScreen() {
       Handler(Looper.getMainLooper()).postDelayed({
           checkUserSession()
       },SPLASH_SCREEN_DURATION)
    }

    private fun checkUserSession() {
        userSession.createSession(this)
        val user = userSession.getUser()
       if(user != Constants.DEFAULT_VALUE){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
       }
        else{
           startActivity(Intent(this,ActivityLogin::class.java))
           finish()
       }

    }
}