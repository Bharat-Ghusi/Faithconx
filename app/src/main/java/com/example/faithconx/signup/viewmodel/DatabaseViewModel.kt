package com.example.faithconx.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.model.User
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DatabaseViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val dataSavingState = MutableLiveData<Boolean>()
    fun getDataSavingState(): LiveData<Boolean> = dataSavingState


    fun saveUserDetailsToDb(
        firstName: String?,
        lastName: String?,
        email: String?,
        number: String?,
        profileUrl: String?,
        firebaseAuth: FirebaseAuth?
    ) {
        //Get hardcoded data
        val user = setFakeSignupData(firstName,lastName,email,number,profileUrl)

        firebaseAuth?.uid?.let {
            firebaseDatabase.purgeOutstandingWrites()
            firebaseDatabase.goOffline()
            firebaseDatabase.goOnline()
            firebaseDatabase.reference.child(Constants.USERS_DB_NAME)
                .child(it).setValue(User(firstName, lastName, email, number, profileUrl))
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        //Save to DB successful
                        dataSavingState.postValue(true)

                    } else {
                        //Save to DB Failed
                        dataSavingState.postValue(false)
                    }
                }
        }
    }

    private fun setFakeSignupData(firstName: String?, lastName: String?, email: String?, number: String?, profileUrl: String?):User {

    }
}