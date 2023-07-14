package com.example.faithconx.util

object Constants {
    val OTP_LENGTH = 6
    val USERS_DB_NAME = "USERS"
    val USERS_DB_CHILD_NAME = "User"
    val AUHTENTICATION_FAILED_MSG = "Authentication failed."
    val USER_ALREADY_LOGGEDIN_MSG = "User already logged in."
    val SUCCESS_MSG_OF_SAVING_USER_DATA = "Successfully save user data into DB."
    val FAILURE_MSG_OF_SAVING_USER_DATA = "Successfully save user data into DB."
    val PROFILE_TYPE = "image/*"
    val CHOOSE_PROFILE_IMG_MSG = "Choose Image."
    val BROWSE_PROFILE_IMAGE_CODE = 1
    val FIELD_CANNOT_BE_EMPTY_MSG = "Field cannot be empty"

    /**
     * LOGIN PAGE
     */
    val EMAIL_OR_PASSWOD_WRONG_MSG = "Email password is wrong."


    /**
     * SIGNUP PAGE
     */
    val WHITE_SPACE_ERROR_MSG = "White Spaces are not allowed."

    //    FIRST NAME
    val FIRSTNAME_TOO_SMALL_MSG = "Firstname too small."
    val FIRSTNAME_TOO_LARGE_MSG = "Firstname too Large."
    val FIRSTNAME_MIN_LENGTH = 3
    val FIRSTNAME_MAX_LENGTH = 20

    //    LAST NAME
    val LASTNAME_TOO_SMALL_MSG = "Lastname too small."
    val LASTNAME_TOO_LARGE_MSG = "Lastname too small."
    val LASTNAME_MIN_LENGTH = 3
    val LASTNAME_MAX_LENGTH = 20

    //    PASSWORD
    val PASSWORD_IS_TOO_WEAK_MSG = "Password is too weak use combination of[1,A,a,@,$,_,]."
    val PASSWORD_NOT_MATCH_MSG = "Password doesn't match."
    //CHECKBOX
    val TERMS_AND_CONDITION_MSG = "Please agree terms and condition."
//EMAIL
    val EMAIL_FORMAT_MSG = "Bad email format."
    //NUMBER
    val INVALID_NUMBER_MSG="Invalid number."
    //USER
    const val USER_SESSION_KEY= "User session."
    val DEFAULT_VALUE= null
    //Fragments
    const val COMING_SOON_MSG = "Coming soon."
}