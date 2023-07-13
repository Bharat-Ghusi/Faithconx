package com.example.faithconx.model

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val firstName: String,val lastName:String,val email:String,
val phoneNumber:String) : Parcelable
