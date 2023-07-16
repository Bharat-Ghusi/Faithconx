package com.example.faithconx.signup.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager

class MediaPermission : Permission {
    override fun isPermissionGranted(context: Context?, permissions: Array<String>): Boolean {
        var isTrue = true
        for (permission in permissions){
            isTrue = isTrue &&  (context?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
        }
        return isTrue
    }

    override fun requestPermission(activity: Activity, permissions: Array<String>, code: Int) {
        activity.requestPermissions(permissions,code)
    }
}