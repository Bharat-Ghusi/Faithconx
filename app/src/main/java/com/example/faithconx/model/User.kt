package com.example.faithconx.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize

data class User(
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var phoneNumber: String? = "",
    var profileUrl: String? = "",
    var followersCount:Int? = 0,
    var followingCount:Int? = 0,
    var designationOne:String? = "",
    var designationTwo:String = "",
    var hobby:String? = "",
    var city: String? = "",
    var state: String? = "",
    var twitterUsername:String? = "",
    var instagramUsername:String? ="",
    var joinedDate:String? = "",
    var revenue:Long? = 0,
    var companyName:String? =""

) : Parcelable
