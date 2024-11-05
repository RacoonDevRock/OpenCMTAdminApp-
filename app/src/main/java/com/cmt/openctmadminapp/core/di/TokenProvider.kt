package com.cmt.openctmadminapp.core.di

import android.content.SharedPreferences

class TokenProvider(private val sharedPreferences: SharedPreferences) {

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun clearToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }
}