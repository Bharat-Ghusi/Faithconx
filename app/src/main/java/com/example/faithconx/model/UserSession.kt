package com.example.faithconx.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserSession(val email:String, val isLoggedIn:Boolean) : Parcelable
