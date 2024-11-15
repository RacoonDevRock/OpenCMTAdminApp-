package com.cmt.openctmadminapp.core.di

import android.content.Context
import android.content.SharedPreferences

class ThemePreferenceManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val THEME_KEY = "theme_key"
        const val LIGHT_MODE = "light"
        const val DARK_MODE = "dark"
        const val DEFAULT_MODE = "default" // Basado en el sistema
    }

    fun saveTheme(theme: String) {
        preferences.edit().putString(THEME_KEY, theme).apply()
    }

    fun getTheme(): String {
        return preferences.getString(THEME_KEY, DEFAULT_MODE) ?: DEFAULT_MODE
    }
}