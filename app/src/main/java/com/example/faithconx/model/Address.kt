package com.example.faithconx.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(var city:String? = "",
var state:String? = "",
var country:String? ="") : Parcelable
