package com.example.faithconx.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel: ViewModel() {
    // LiveData for email and password fields
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // LiveData for authentication state
    private val _authenticationState = MutableLiveData<Boolean>()
    val authenticationState: LiveData<Boolean>
        get() = _authenticationState

    private val auth = FirebaseAuth.getInstance()

    fun login() {
        val emailValue = email.value
        val passwordValue = password.value

        if (emailValue != null && passwordValue != null) {
            auth.signInWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authenticationState.value = true // Login success
                    } else {
                        _authenticationState.value = false // Login failed
                    }
                }
        } else {
            _authenticationState.value = false // Login failed (invalid email or password)
        }
    }

    fun signup() {
        val emailValue = email.value
        val passwordValue = password.value

        if (emailValue != null && passwordValue != null) {
            auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authenticationState.value = true // Signup success
                    } else {
                        _authenticationState.value = false // Signup failed
                    }
                }
        } else {
            _authenticationState.value = false // Signup failed (invalid email or password)
        }
    }
}


