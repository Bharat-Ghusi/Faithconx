package com.example.faithconx.helper.user

import android.content.Context
import android.renderscript.ScriptGroup.Input
import android.text.InputFilter
import android.util.Log
import com.example.faithconx.R
import com.example.faithconx.util.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener

class UserValidation(val context: Context) {
    fun validateFirstName(editText: TextInputEditText,editTextLayout: TextInputLayout): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editTextLayout.error = context.getString(R.string.field_cannot_be_empty)
            return false
        } else if (text.length < Constants.FIRSTNAME_MIN_LENGTH) {
            editTextLayout.error =context.getString(R.string.firstname_too_small)
            return false
        } else if (text.length > Constants.FIRSTNAME_MAX_LENGTH) {
            editTextLayout.error = context.getString(R.string.firstname_too_large)
            return false
        }
        /**
         * "\A"     -> denotes the start of the string.
        "\w"    -> matches any word character (alphanumeric character or underscore).
        "{4,20}"-> specifies that the previous pattern (\w) should occur between 4 and 20 times OR  has a length between 4 and 20 characters (inclusive).
        "\z"    ->represents the end of the string OR  It ensures that the entire string matches this pattern from start to end..
         */
        else if (!text.matches(Regex("\\A\\w+\\z"))) {
            editTextLayout.error = context.getString(R.string.white_spaces_are_not_allowed)
            return false
        }

        return true
    }

    fun validateLastName(editText: TextInputEditText): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editText.error = Constants.FIELD_CANNOT_BE_EMPTY_MSG
            return false
        } else if (text.length < Constants.LASTNAME_MIN_LENGTH) {
            editText.error = Constants.LASTNAME_TOO_SMALL_MSG
            return false
        } else if (text.length > Constants.LASTNAME_MAX_LENGTH) {
            editText.error = Constants.LASTNAME_TOO_LARGE_MSG
        }
        /**
         * "\A"     -> denotes the start of the string.
        "\w"    -> matches any word character (alphanumeric character or underscore).
        "{4,20}"-> specifies that the previous pattern (\w) should occur between 4 and 20 times OR  has a length between 4 and 20 characters (inclusive).
        "\z"    ->represents the end of the string OR  It ensures that the entire string matches this pattern from start to end..
         */
        else if (!text.matches(Regex("\\A\\w{4,20}\\z"))) {
            editText.error = Constants.WHITE_SPACE_ERROR_MSG
            return false
        }else
            editText.error = null
        return true
    }

    fun validateEmail(editText: TextInputEditText,editTextLayout: TextInputLayout): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editTextLayout.error = context.getString(R.string.field_cannot_be_empty)
            return false
        }
        /**
        [a-zA-Z0-9._-]+: This part matches one or more occurrences of letters (both lowercase and uppercase), digits,
        dots, underscores, or hyphens. It represents the username portion of the email address before the @ symbol.

        @: This matches the literal @ symbol.

        [a-z]+: This matches one or more occurrences of lowercase letters only.
        It represents the domain name portion of the email address after the @ symbol.

        \\.: This matches a literal dot (.). The double backslash is an escape sequence
        to represent a single backslash in the regular expression.

        [a-z]+: This matches one or more occurrences of lowercase letters only. It represents
        the top-level domain portion of the email address after the dot.
         */
        else if (!text.matches(Regex("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}"))) {
            editTextLayout.error = context.getString(R.string.bad_email_format)
            return false
        }
        else
            editTextLayout.error = null
        return true
    }
    fun validatePhoneNumber(editText: TextInputEditText,editTextLayout: TextInputLayout,ccp: CountryCodePicker
    ): Boolean {
        var isValid = false
//        Validity Change Listener will get callBack every time validity of entered number changes.
        //called only when entered number is valid.

        ccp.setPhoneNumberValidityChangeListener {
            if(!it){
                editText.filters =arrayOf<InputFilter>()
                editTextLayout.error =Constants.INVALID_NUMBER_MSG
                isValid=false

            }else{
                //Number is valid.
                // Create an InputFilter to constrain the length limit
                val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(editText.text.toString().length))
                editText.filters = filters
                editTextLayout.error = null
                isValid=true

            }
        }

        return isValid
    }
    fun validatePassword(editText: TextInputEditText,editTextLayout: TextInputLayout): Boolean {
        val text = editText.text.toString()
        val regex = Regex(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"
        )

        //Check empty
        if(text.isEmpty()){
            editTextLayout.error = context.getString(R.string.field_cannot_be_empty)
            return false
        }
        //isValid
        else if(! text.matches(regex)){
            editTextLayout.error = context.getString(R.string.password_is_too_weak)
            return false
        }

        return true
    }
    fun validateConfirmPassword(etPassword: TextInputEditText,etConfirmPassword:TextInputEditText,editTextLayout: TextInputLayout): Boolean {
        val confirmPassword = etConfirmPassword.text.toString()
        val password = etPassword.text.toString()
        val regex = Regex(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"
        )

        //Check empty
        if (confirmPassword.isEmpty()){
            editTextLayout.error = context.getString(R.string.field_cannot_be_empty)
            return false
        }
        // isValid
        else if (! confirmPassword.matches(regex)){
            editTextLayout.error = context.getString(R.string.password_is_too_weak)
            return false
        }
        return true
    }
}