package com.example.faithconx.signup.helper

import android.app.Activity
import android.content.Context

interface Permission {
    fun isPermissionGranted(context: Context?,permissions:Array<String>):Boolean
    fun requestPermission(activity: Activity,permissions:Array<String>,code:Int)
}