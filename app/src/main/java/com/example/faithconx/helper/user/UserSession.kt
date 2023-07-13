package com.example.faithconx.helper.user

import android.content.Context
import android.content.SharedPreferences

import androidx.preference.PreferenceManager
import com.example.faithconx.model.User
import com.example.faithconx.model.UserSession
import com.example.faithconx.util.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class UserSession() {
    private lateinit var sharedPreference: SharedPreferences
    fun createSession(context: Context?) {
        sharedPreference = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!

    }
    fun setUser(userSession: UserSession) {

        val editor = sharedPreference.edit()
        val gson = Gson()
        val toJson = gson.toJson(userSession)
        editor.putString(Constants.USER_SESSION_KEY, toJson).apply()
    }

    fun getUser(): UserSession? {
        val gson = Gson()
        val json = sharedPreference.getString(Constants.USER_SESSION_KEY, Constants.DEFAULT_VALUE)
        return gson.fromJson(json, object : TypeToken<UserSession>() {}.type)
    }
    fun removeUser(){
        sharedPreference.edit().remove(Constants.USER_SESSION_KEY).apply()

    }
}