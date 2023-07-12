package com.example.faithconx.signup.helper

import com.example.faithconx.helper.user.UserValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UserHelper {
    private val userValidation = UserValidation()

    fun etFirstNameFocusChange(
        hasFocus: Boolean,
        tilFirstName: TextInputLayout,
        etFirstName: TextInputEditText
    ) {
        if (!hasFocus) {
            userValidation.validateFirstName(etFirstName, tilFirstName)
        }
    }

    fun etEmailFocusChange(
        hasFocus: Boolean,
        tilEmail: TextInputLayout,
        etEmail: TextInputEditText
    ) {
        if (!hasFocus) {
            userValidation.validateEmail(etEmail, tilEmail)
        }
    }

    fun etPhoneNumberFocusChange(
        hasFocus: Boolean,
        tilPhoneNumber: TextInputLayout,
        etPhoneNumber: TextInputEditText
    ) {
        if (!hasFocus) {
            userValidation.validatePhoneNumber(etPhoneNumber, tilPhoneNumber)
        }
    }

    fun etPasswordFocusChange(
        hasFocus: Boolean,
        tilPassword: TextInputLayout,
        etPassword: TextInputEditText
    ) {
        if (!hasFocus) {
            userValidation.validatePassword(etPassword, tilPassword)
        }
    }

    fun etConfirmPasswordFocusChange(
        hasFocus: Boolean,
        tilConfirmPassword: TextInputLayout,
        etConfirmPassword: TextInputEditText,
        etPassword: TextInputEditText
    ) {
        if (!hasFocus) {
            userValidation.validateConfirmPassword(
                etPassword,
                etConfirmPassword,
                tilConfirmPassword
            )
        }
    }

}