package com.example.faithconx.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val profileUrl: String?
) : Parcelable{

}
