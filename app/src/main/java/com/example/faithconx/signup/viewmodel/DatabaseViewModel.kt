package com.example.faithconx.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faithconx.model.User
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ThreadLocalRandom

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
                .child(it).setValue(user)
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
        val user = User(firstName,lastName,email,number,profileUrl)
            user.followersCount = ThreadLocalRandom.current().nextInt(1000,2000 +1)
        user.followingCount = ThreadLocalRandom.current().nextInt(1000,2000 +1)
        user.designationOne = "CEO"
        user.designationTwo = "Co-founder"
        user.hobby = "Travellers + 70 country."
        user.state = "Odisha"
        user.city ="Jharsuguda"
        user.twitterUsername = firstName?.lowercase()
        user.instagramUsername = firstName?.lowercase()
        user.joinedDate =getCurrentDateFormatted()
        user.revenue = ThreadLocalRandom.current().nextLong(100000,2000000 +1)
        user.companyName = "Appscrip"
        return user
    }
    private fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("d MMMM, yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}