package com.example.faithconx.helper.user

import com.example.faithconx.util.Constants
import com.google.android.material.textfield.TextInputEditText

class UserValidation {
    fun validateFirstName(editText: TextInputEditText): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editText.error = Constants.FIELD_CANNOT_BE_EMPTY_MSG
            return false
        } else if (text.length < Constants.FIRSTNAME_MIN_LENGTH) {
            editText.error = Constants.FIRSTNAME_TOO_SMALL_MSG
            return false
        } else if (text.length < Constants.FIRSTNAME_MAX_LENGTH) {
            editText.error = Constants.FIRSTNAME_TOO_LARGE_MSG
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
        } else
            editText.error = null

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
        } else if (text.length < Constants.LASTNAME_MAX_LENGTH) {
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

    fun validateEmail(editText: TextInputEditText): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editText.error = Constants.FIELD_CANNOT_BE_EMPTY_MSG
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
        else if (!text.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+"))) {
            editText.error = Constants.WHITE_SPACE_ERROR_MSG
            return false
        }
        else
            editText.error = null
        return true
    }
    fun validatePhoneNumber(editText: TextInputEditText): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editText.error = Constants.FIELD_CANNOT_BE_EMPTY_MSG
            return false
        }

        else
            editText.error = null
        return true
    }
    fun validatePassword(editText: TextInputEditText): Boolean {
        val text = editText.text.toString()
        val regex = Regex(
            "^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"
        )
        if (text.isEmpty()) {
            editText.error = Constants.FIELD_CANNOT_BE_EMPTY_MSG
            return false
        }
        else if(! text.matches(regex)){
            editText.error = Constants.PASSWORD_IS_TOO_WEAK_MSG
        }

        else
            editText.error = null
        return true
    }
    fun validateConfirmPassword(etPassword: TextInputEditText,etConfirmPassword:TextInputEditText): Boolean {
        if(etPassword.text.toString() != etConfirmPassword.text.toString()){
            etConfirmPassword.error = Constants.PASSWORD_NOT_MATCH_MSG
            return  false
        }
        return true
    }
}