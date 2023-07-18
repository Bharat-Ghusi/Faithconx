package com.example.faithconx.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Location(
    var city: String,
    val coordinates:@RawValue Coordinates,
    var country: String,
    val postcode: String,
    var state: String,
    val street:@RawValue Street,
    val timezone: @RawValue Timezone
) : Parcelable